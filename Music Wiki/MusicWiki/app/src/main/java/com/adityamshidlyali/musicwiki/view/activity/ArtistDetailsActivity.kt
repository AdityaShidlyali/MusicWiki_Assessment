package com.adityamshidlyali.musicwiki.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.adapter.ArtistTopItemsViewPager2Adapter
import com.adityamshidlyali.musicwiki.databinding.ActivityArtistDetailsBinding
import com.adityamshidlyali.musicwiki.model.tagmodel.TagArtistInfo
import com.adityamshidlyali.musicwiki.network.EndPoints
import com.adityamshidlyali.musicwiki.utility.Utility.formatNumbers
import com.adityamshidlyali.musicwiki.utility.Utility.removeUrlFromString
import com.adityamshidlyali.musicwiki.viewmodel.ArtistInfoViewModel
import com.adityamshidlyali.musicwiki.viewmodel.ArtistTopTagsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Activity which displays the Artist's information
 */
@AndroidEntryPoint
class ArtistDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtistDetailsBinding

    private val viewModel: ArtistInfoViewModel by viewModels()

    private val topTagsViewModel: ArtistTopTagsViewModel by viewModels()

    private val tabLayoutTitles = listOf<String>("TOP ALBUMS", "TOP TRACKS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * AlertDialog to show the loading state of the tag lists from the network
         */
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(layoutInflater.inflate(R.layout.loading_dialog, null))
        alertDialogBuilder.setCancelable(false)
        val loadingDialog = alertDialogBuilder.create()

        /**
         * created artist parcelable got from the Artist's Fragment
         * to show user's clicked album from the list of artist
         */
        val artistParcelable =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("artist_parcelable", TagArtistInfo::class.java)
            } else {
                intent.getParcelableExtra<TagArtistInfo>("artist_parcelable")
            }

        /**
         * Load the image from network call or else apply the default image
         */
        binding.apply {
            Glide.with(baseContext)
                .load(artistParcelable!!.image[3].imageLink)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_music_default)
                .into(ivArtistImage)

            tvArtisTitle.text = artistParcelable.name
        }

        /**
         * removed unnecessary characters from string for network calls
         */
        val artistNameForNetworkCall = artistParcelable?.name.toString()
            .replace(" ", "")
            .replace("-", "")
            .replaceFirstChar { it.lowercase() }

        /**
         * Launch the coroutine to get the artist's information
         */
        lifecycleScope.launch {
            viewModel.getArtistInfo(
                EndPoints.API_KEY, EndPoints.FORMAT, EndPoints.ARTIST_INFO, artistNameForNetworkCall
            )

            viewModel.artistInfoStateFlow.collect {
                when (it) {
                    is ArtistInfoViewModel.GetArtistInfoEvent.Success -> {
                        binding.apply {
                            tvArtistFollowers.text =
                                formatNumbers(it.artistInfo.stats.listeners.toLong())
                            tvArtistPlaycount.text =
                                formatNumbers(it.artistInfo.stats.playcount.toLong())
                            tvArtisDetails.text =
                                removeUrlFromString(it.artistInfo.bio.summary)
                        }
                    }
                    else -> {}
                }
            }
        }

        /**
         * Launch the coroutine to get the artist's top tags information
         */
        lifecycleScope.launch {
            topTagsViewModel.getArtistTopTags(
                EndPoints.API_KEY,
                EndPoints.FORMAT,
                EndPoints.ARTIST_TOP_TAGS,
                artistNameForNetworkCall
            )

            topTagsViewModel.artistTopTagsStateFlow.collect {
                when (it) {

                    is ArtistTopTagsViewModel.GetArtistTopTagsEvent.Loading -> {
                        if (!loadingDialog.isShowing) {
                            loadingDialog.show()
                        }
                    }

                    is ArtistTopTagsViewModel.GetArtistTopTagsEvent.Failure -> {
                        Toast.makeText(
                            this@ArtistDetailsActivity,
                            "Something went wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is ArtistTopTagsViewModel.GetArtistTopTagsEvent.Success -> {
                        if (loadingDialog.isShowing) {
                            loadingDialog.dismiss()
                        }

                        /**
                         * Populate the chips using the objects hashcode as unique ID
                         * to handle the clicks of chips from chipgroup uniquely
                         */
                        val genresChips = ArrayList<Chip>()
                        for (i in it.topTagList.tag) {
                            val genreChip = Chip(this@ArtistDetailsActivity)
                            genreChip.text = i.name.replaceFirstChar { it ->
                                it.titlecase()
                            }
                            genreChip.id = i.hashCode()
                            genresChips.add(genreChip)
                        }

                        runOnUiThread {
                            for (i in 0 until genresChips.size) {
                                binding.chipGroupArtistTopGenres.addView(genresChips[i])
                            }
                        }
                    }
                    else -> {}
                }
            }
        }

        /**
         * handle the clicks of chips from chipgroup
         * and launch the GenreDetailsActivity to show the details of the Genres selected by user using chips
         */
        binding.chipGroupArtistTopGenres.setOnCheckedStateChangeListener { group, _ ->
            val selectedChipId = group.checkedChipId
            val selectedChip = group.findViewById<Chip>(selectedChipId)
            selectedChip.isChecked = false
            selectedChip.setOnClickListener {
                val intent =
                    Intent(this@ArtistDetailsActivity, GenreTagDetailsActivity::class.java)
                intent.putExtra(
                    "genre_tag",
                    selectedChip.text.toString()
                )
                startActivity(intent)
            }
        }

        /**
         * create bundle to put the artist's names for further network calls
         * and this bundle is passed to the ArtistTopItemsViewPager2Adapter
         */
        val bundle = Bundle()
        bundle.putString("artist_name", artistNameForNetworkCall)

        val viewPager2 = binding.viewpager2TopTracksTopGenres
        val tabLayout = binding.tabLayout
        val genreItemsViewPager2Adapter =
            ArtistTopItemsViewPager2Adapter(supportFragmentManager, lifecycle, bundle)
        viewPager2.adapter = genreItemsViewPager2Adapter

        /**
         * populate the tab layout
         */
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()
    }
}
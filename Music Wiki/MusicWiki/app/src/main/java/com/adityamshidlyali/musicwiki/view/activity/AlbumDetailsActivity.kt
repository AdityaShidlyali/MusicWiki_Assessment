package com.adityamshidlyali.musicwiki.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.databinding.ActivityAlbumDetailsBinding
import com.adityamshidlyali.musicwiki.model.tagmodel.TagAlbumInfo
import com.adityamshidlyali.musicwiki.network.EndPoints
import com.adityamshidlyali.musicwiki.utility.Utility.removeUrlFromString
import com.adityamshidlyali.musicwiki.viewmodel.AlbumInfoViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Activity which displays the album's information
 */
@AndroidEntryPoint
class AlbumDetailsActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: ActivityAlbumDetailsBinding

    private val viewModel: AlbumInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * created album parcelable got from the AlbumsFragment
         * to show user's clicked album from the list of albums
         */
        val albumParcelable =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("album_parcelable", TagAlbumInfo::class.java)
            } else {
                intent.getParcelableExtra<TagAlbumInfo>("album_parcelable")
            }

        /**
         * Load the image from network call or else apply the default image
         */
        binding.apply {
            Glide.with(baseContext)
                .load(albumParcelable!!.image[3].imageLink)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_music_default)
                .into(ivAlbumImage)

            tvAlbumTitle.text = albumParcelable.name
        }

        /**
         * removed unnecessary characters from string for network calls
         */
        val albumNameForNetworkCall = albumParcelable?.name.toString()
            .replace(" ", "")
            .replace("-", "")
            .replaceFirstChar { it.lowercase() }

        val artistNameForNetworkCall = albumParcelable?.artist?.name.toString()
            .replace(" ", "")
            .replace("-", "")
            .replaceFirstChar { it.lowercase() }

        /**
         * Launch the coroutine to get the albums information
         */
        lifecycleScope.launch {
            viewModel.getAlbumInfo(
                EndPoints.API_KEY,
                EndPoints.FORMAT,
                EndPoints.ALBUM_INFO,
                artistNameForNetworkCall,
                albumNameForNetworkCall
            )

            viewModel.albumInfoStateFlow.collect {
                when (it) {
                    is AlbumInfoViewModel.GetAlbumInfoEvent.Success -> {
                        binding.tvAlbumDetails.text =
                            removeUrlFromString("${it.albumDetails.wiki.published} " + it.albumDetails.wiki.summary)

                        val genresChips = ArrayList<Chip>()
                        for (i in it.albumDetails.tags.tag) {
                            val genreChip = Chip(this@AlbumDetailsActivity)
                            genreChip.text = i.name.replaceFirstChar { it ->
                                it.titlecase()
                            }
                            genreChip.id = i.hashCode()
                            genresChips.add(genreChip)
                        }

                        runOnUiThread {
                            for (i in 0 until genresChips.size) {
                                binding.chipGroupAlbumGenres.addView(genresChips[i])
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
        binding.chipGroupAlbumGenres.setOnCheckedStateChangeListener { group, _ ->
            val selectedChipId = group.checkedChipId
            val selectedChip = group.findViewById<Chip>(selectedChipId)
            selectedChip.isChecked = false
            selectedChip.setOnClickListener {
                val intent =
                    Intent(this@AlbumDetailsActivity, GenreTagDetailsActivity::class.java)
                intent.putExtra(
                    "genre_tag",
                    selectedChip.text.toString()
                )
                startActivity(intent)
            }
        }
    }
}
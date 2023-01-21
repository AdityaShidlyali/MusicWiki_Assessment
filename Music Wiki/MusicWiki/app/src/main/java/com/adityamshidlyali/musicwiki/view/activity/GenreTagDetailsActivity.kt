package com.adityamshidlyali.musicwiki.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.adapter.GenreItemsViewPager2Adapter
import com.adityamshidlyali.musicwiki.databinding.ActivityGenreTagDetailsBinding
import com.adityamshidlyali.musicwiki.network.EndPoints
import com.adityamshidlyali.musicwiki.utility.Utility.removeUrlFromString
import com.adityamshidlyali.musicwiki.viewmodel.GenreTagDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Activity which displays the tag's (Genre's) information
 */
@AndroidEntryPoint
class GenreTagDetailsActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: ActivityGenreTagDetailsBinding

    private val viewModel: GenreTagDetailsViewModel by viewModels()

    private val tabLayoutTitles = listOf<String>("ALBUMS", "ARTISTS", "TRACKS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreTagDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * AlertDialog to show the loading state of the tag lists from the network
         */
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(layoutInflater.inflate(R.layout.loading_dialog, null))
        alertDialogBuilder.setCancelable(false)
        val loadingDialog = alertDialogBuilder.create()

        val genreTitleString = intent.getStringExtra("genre_tag").toString()

        binding.toolbar.title = genreTitleString

        /**
         * removed unnecessary characters from string for network calls
         */
        val tagForNetworkCall = genreTitleString
            .replace(" ", "")
            .replace("-", "")
            .replaceFirstChar { it.lowercase() }

        /**
         * create bundle to put the artist's names for further network calls
         * and this bundle is passed to the ArtistTopItemsViewPager2Adapter
         */
        val bundle = Bundle()
        bundle.putString("tag_genre", tagForNetworkCall)

        /**
         * Launch the coroutine to get the tag's information
         */
        lifecycleScope.launch {

            viewModel.getTagInfo(
                EndPoints.API_KEY,
                EndPoints.FORMAT,
                tagForNetworkCall,
                EndPoints.TAG_INFO
            )

            viewModel.genreTagDetailsStateFlow.collect {
                when (it) {
                    is GenreTagDetailsViewModel.GetGenreTagDetailsEvent.Loading -> {
                        if (!loadingDialog.isShowing) {
                            loadingDialog.show()
                        }
                    }

                    is GenreTagDetailsViewModel.GetGenreTagDetailsEvent.Failure -> {
                        Toast.makeText(
                            this@GenreTagDetailsActivity,
                            "Something went wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is GenreTagDetailsViewModel.GetGenreTagDetailsEvent.Success -> {
                        if (loadingDialog.isShowing) {
                            loadingDialog.dismiss()
                        }
                        binding.genreDescription.text = removeUrlFromString(it.genreTagSummary)
                    }
                    else -> {}
                }
            }
        }

        val viewPager2 = binding.viewpager2
        val tabLayout = binding.tabLayout
        val genreItemsViewPager2Adapter =
            GenreItemsViewPager2Adapter(supportFragmentManager, lifecycle, bundle)
        viewPager2.adapter = genreItemsViewPager2Adapter

        /**
         * populate the tab layout
         */
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()
    }
}
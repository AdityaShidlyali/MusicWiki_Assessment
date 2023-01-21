package com.adityamshidlyali.musicwiki.view.activity

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.databinding.ActivityMainBinding
import com.adityamshidlyali.musicwiki.network.EndPoints
import com.adityamshidlyali.musicwiki.viewmodel.GenreTagsViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: ActivityMainBinding

    private val viewModel: GenreTagsViewModel by viewModels()

    private val genresChips = ArrayList<Chip>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * AlertDialog to show the loading state of the tag lists from the network
         */
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(layoutInflater.inflate(R.layout.loading_dialog, null))
        alertDialogBuilder.setCancelable(false)
        val loadingDialog = alertDialogBuilder.create()

        /**
         * minimize the hidden chips to show only the top10 genres
         */
        binding.chipGroupGenresExpanded.visibility = View.GONE
        binding.bExpandGenres.icon =
            ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_expand_more)
        binding.bExpandGenres.text = baseContext.getString(R.string.more_genres)

        /**
         * handle the click of more/less button to expand cardview and show other genres
         */
        binding.bExpandGenres.setOnClickListener {
            if (!binding.chipGroupGenresExpanded.isVisible) {
                TransitionManager.beginDelayedTransition(binding.cardViewGenres, AutoTransition())
                binding.chipGroupGenresExpanded.visibility = View.VISIBLE
                binding.bExpandGenres.icon =
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_expand_less)
                binding.bExpandGenres.text = baseContext.getString(R.string.less_genres)
            } else {
                TransitionManager.beginDelayedTransition(binding.cardViewGenres, AutoTransition())
                binding.chipGroupGenresExpanded.visibility = View.GONE
                binding.bExpandGenres.icon =
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_expand_more)
                binding.bExpandGenres.text = baseContext.getString(R.string.more_genres)
            }
        }

        /**
         * Launch the coroutine to get the list tags(Genres)
         */
        lifecycleScope.launch {
            viewModel.getGenreTags(EndPoints.API_KEY, EndPoints.FORMAT, EndPoints.TOP_TAGS)

            viewModel.genreListFetchStateFlow.collect {
                when (it) {
                    is GenreTagsViewModel.GetGenreTagsEvent.Loading -> {
                        if (!loadingDialog.isShowing) {
                            loadingDialog.show()
                        }
                    }

                    /**
                     * Populate the chips using the objects hashcode as unique ID
                     * to handle the clicks of chips from chipgroup uniquely
                     */
                    is GenreTagsViewModel.GetGenreTagsEvent.Success -> {
                        if (loadingDialog.isShowing) {
                            loadingDialog.dismiss()
                        }
                        for (i in it.genreTags!!) {
                            val genreChip = Chip(this@MainActivity)
                            genreChip.text = i.name.replaceFirstChar { it ->
                                it.titlecase()
                            }
                            genreChip.id = i.hashCode()
                            genresChips.add(genreChip)
                        }

                        runOnUiThread {
                            for (i in 0..9) {
                                binding.chipGroupGenresTop10.addView(genresChips[i])
                            }

                            for (i in 10 until genresChips.size) {
                                binding.chipGroupGenresExpanded.addView(genresChips[i])
                            }
                        }
                    }
                    is GenreTagsViewModel.GetGenreTagsEvent.Failure -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Something went wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {

                    }
                }
            }
        }

        /**
         * handle the clicks of chips from chipgroup
         * and launch the GenreDetailsActivity to show the details of the Genres selected by user using chips
         *
         * only for top10 genres displayed
         */
        binding.chipGroupGenresTop10.setOnCheckedStateChangeListener { group, _ ->
            val selectedChipId = group.checkedChipId
            val selectedChip = group.findViewById<Chip>(selectedChipId)
            selectedChip.isChecked = false
            selectedChip.setOnClickListener {
                val intent = Intent(this@MainActivity, GenreTagDetailsActivity::class.java)
                intent.putExtra(
                    "genre_tag",
                    selectedChip.text.toString()
                )
                startActivity(intent)
            }
        }

        /**
         * handle the clicks of chips from chipgroup
         * and launch the GenreDetailsActivity to show the details of the Genres selected by user using chips
         *
         * only for other genres displayed expect top 10 genres
         */
        binding.chipGroupGenresExpanded.setOnCheckedStateChangeListener { group, _ ->
            val selectedChipId = group.checkedChipId
            val selectedChip = group.findViewById<Chip>(selectedChipId)
            selectedChip.isChecked = false
            selectedChip.setOnClickListener {
                val intent = Intent(this@MainActivity, GenreTagDetailsActivity::class.java)
                intent.putExtra(
                    "genre_tag",
                    selectedChip.text.toString()
                )
                startActivity(intent)
            }
        }
    }
}
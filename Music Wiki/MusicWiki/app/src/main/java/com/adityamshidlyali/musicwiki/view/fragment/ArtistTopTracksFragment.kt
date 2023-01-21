package com.adityamshidlyali.musicwiki.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.adapter.ArtistTopTracksItemAdapter
import com.adityamshidlyali.musicwiki.adapter.PagingLoadStateAdapter
import com.adityamshidlyali.musicwiki.databinding.FragmentArtistTopTracksBinding
import com.adityamshidlyali.musicwiki.viewmodel.ArtistTopTracksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment to show the list of artist's top tracks inside the viewpager
 */
@AndroidEntryPoint
class ArtistTopTracksFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private val viewModel: ArtistTopTracksViewModel by viewModels()

    private var _binding: FragmentArtistTopTracksBinding? = null

    private val binding
        get() = _binding!!

    private var artist = "coldplay"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        artist = bundle!!.getString("artist_name").toString()
        Log.d(TAG, artist)
        return inflater.inflate(R.layout.fragment_artist_top_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentArtistTopTracksBinding.bind(view)

        val adapter = ArtistTopTracksItemAdapter()

        binding.apply {
            rvArtistTopTracks.layoutManager = GridLayoutManager(context, 2)
            rvArtistTopTracks.setHasFixedSize(true)
            rvArtistTopTracks.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { adapter.retry() },
                footer = PagingLoadStateAdapter { adapter.retry() }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArtistTopTracks(artist).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
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
import com.adityamshidlyali.musicwiki.adapter.ArtistTopAlbumsItemAdapter
import com.adityamshidlyali.musicwiki.databinding.FragmentArtistTopAlbumBinding
import com.adityamshidlyali.musicwiki.viewmodel.ArtistTopAlbumsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment to show the list of artist's top albums inside the viewpager
 */
@AndroidEntryPoint
class ArtistTopAlbumFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private val viewModel: ArtistTopAlbumsViewModel by viewModels()

    private var _binding: FragmentArtistTopAlbumBinding? = null

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
        return inflater.inflate(R.layout.fragment_artist_top_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentArtistTopAlbumBinding.bind(view)

        val adapter = ArtistTopAlbumsItemAdapter()

        binding.apply {
            rvArtistTopAlbums.layoutManager = GridLayoutManager(context, 2)
            rvArtistTopAlbums.setHasFixedSize(true)
            rvArtistTopAlbums.adapter = adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getArtistTopAlbums(artist).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
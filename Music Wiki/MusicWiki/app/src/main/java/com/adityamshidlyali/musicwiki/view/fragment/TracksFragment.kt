package com.adityamshidlyali.musicwiki.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.adapter.PagingLoadStateAdapter
import com.adityamshidlyali.musicwiki.adapter.TagTracksItemAdapter
import com.adityamshidlyali.musicwiki.databinding.FragmentTracksBinding
import com.adityamshidlyali.musicwiki.viewmodel.TagTracksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment to show the list of tracks inside the viewpager
 */
@AndroidEntryPoint
class TracksFragment : Fragment() {

    private val viewModel: TagTracksViewModel by viewModels()

    private var _binding: FragmentTracksBinding? = null

    private val binding
        get() = _binding!!

    private var genreTag: String = "rock"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        genreTag = bundle!!.getString("tag_genre").toString()
        return inflater.inflate(R.layout.fragment_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTracksBinding.bind(view)

        val adapter = TagTracksItemAdapter()

        binding.apply {
            rvTagTracks.layoutManager = GridLayoutManager(context, 2)
            rvTagTracks.setHasFixedSize(true)
            rvTagTracks.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { adapter.retry() },
                footer = PagingLoadStateAdapter { adapter.retry() }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTagTracks(genreTag).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
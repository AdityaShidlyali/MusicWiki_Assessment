package com.adityamshidlyali.musicwiki.view.fragment

import android.content.Intent
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
import com.adityamshidlyali.musicwiki.adapter.TagArtistsItemAdapter
import com.adityamshidlyali.musicwiki.databinding.FragmentArtistsBinding
import com.adityamshidlyali.musicwiki.model.tagmodel.TagArtistInfo
import com.adityamshidlyali.musicwiki.view.activity.ArtistDetailsActivity
import com.adityamshidlyali.musicwiki.viewmodel.TagArtistsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment to show the list of artists inside the viewpager
 */
@AndroidEntryPoint
class ArtistsFragment : Fragment(), TagArtistsItemAdapter.OnTrackItemClickListener {

    private val viewModel: TagArtistsViewModel by viewModels()

    private var _binding: FragmentArtistsBinding? = null

    private val binding
        get() = _binding!!

    private var genreTag: String = "rock"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bundle = arguments
        genreTag = bundle!!.getString("tag_genre").toString()
        return inflater.inflate(R.layout.fragment_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentArtistsBinding.bind(view)

        val adapter = TagArtistsItemAdapter(this)

        binding.apply {
            rvTagArtists.layoutManager = GridLayoutManager(context, 2)
            rvTagArtists.setHasFixedSize(true)
            rvTagArtists.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { adapter.retry() },
                footer = PagingLoadStateAdapter { adapter.retry() }
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTagArtists(genreTag).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onTrackItemClick(artist: TagArtistInfo) {
        val intent = Intent(activity, ArtistDetailsActivity::class.java)
        intent.putExtra("artist_parcelable", artist)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
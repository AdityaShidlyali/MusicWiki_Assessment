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
import com.adityamshidlyali.musicwiki.adapter.TagAlbumsItemAdapter
import com.adityamshidlyali.musicwiki.databinding.FragmentAlbumsBinding
import com.adityamshidlyali.musicwiki.model.tagmodel.TagAlbumInfo
import com.adityamshidlyali.musicwiki.view.activity.AlbumDetailsActivity
import com.adityamshidlyali.musicwiki.viewmodel.TagAlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment to show the list of albums inside the viewpager
 */
@AndroidEntryPoint
class AlbumsFragment : Fragment(), TagAlbumsItemAdapter.OnAlbumItemClickListener {

    private val viewModel: TagAlbumViewModel by viewModels()

    private var _binding: FragmentAlbumsBinding? = null

    private val binding
        get() = _binding!!

    private var genreTag: String = "rock"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        genreTag = bundle!!.getString("tag_genre").toString()
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAlbumsBinding.bind(view)

        val adapter = TagAlbumsItemAdapter(this)

        binding.apply {
            rvTagAlbums.layoutManager = GridLayoutManager(context, 2)
            rvTagAlbums.setHasFixedSize(true)
            rvTagAlbums.adapter = adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTagAlbums(genreTag).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAlbumItemClick(album: TagAlbumInfo) {
        val intent = Intent(activity, AlbumDetailsActivity::class.java)
        intent.putExtra("album_parcelable", album)
        startActivity(intent)
    }
}
package com.adityamshidlyali.musicwiki.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.databinding.ArtistTopAlbumItemBinding
import com.adityamshidlyali.musicwiki.model.artistmodel.TopAlbum
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Paging Adapter for displaying the list of Artist top albums
 */
class ArtistTopAlbumsItemAdapter :
    PagingDataAdapter<TopAlbum, ArtistTopAlbumsItemAdapter.TopAlbumViewHolder>(TOP_ALBUM_COMPARATOR) {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAlbumViewHolder {
        val binding =
            ArtistTopAlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d(TAG, "Hello")

        return TopAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopAlbumViewHolder, position: Int) {
        val currentItem = getItem(position)

        Log.d(TAG, "Hello")

        holder.bind(currentItem!!)
    }

    inner class TopAlbumViewHolder(
        private val binding: ArtistTopAlbumItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: TopAlbum) {
            binding.apply {
                Glide.with(itemView)
                    .load(album.image[3].imageLink)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_music_default)
                    .into(artistTopAlbumImage)

                tvArtistTopAlbumName.text = album.name
            }

            Log.d(TAG, "Hello")
        }
    }

    companion object {
        private val TOP_ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<TopAlbum>() {

            override fun areItemsTheSame(oldItem: TopAlbum, newItem: TopAlbum) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: TopAlbum, newItem: TopAlbum) =
                oldItem == newItem
        }
    }
}
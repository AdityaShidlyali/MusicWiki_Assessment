package com.adityamshidlyali.musicwiki.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.databinding.TagTrackInfoItemBinding
import com.adityamshidlyali.musicwiki.model.tagmodel.TagTrackInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Paging Adapter for displaying the list of Genre's albums
 */
class TagTracksItemAdapter :
    PagingDataAdapter<TagTrackInfo, TagTracksItemAdapter.AlbumViewHolder>(ALBUM_COMPARATOR) {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding =
            TagTrackInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d("TagAlbumsItemAdapter", "Hello")

        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = getItem(position)

        Log.d("TagAlbumsItemAdapter", "Hello")

        holder.bind(currentItem!!)
    }

    inner class AlbumViewHolder(
        private val binding: TagTrackInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: TagTrackInfo) {
            binding.apply {
                Glide.with(itemView)
                    .load(track.image[3].imageLink)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_music_default)
                    .into(tagAlbumImage)

                tvTagTrackItemTrackName.text = track.name
                tvTagTrackItemArtistName.text = track.artist.name
            }

            Log.d(TAG, "Hello")
        }
    }

    companion object {
        private val ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<TagTrackInfo>() {

            override fun areItemsTheSame(oldItem: TagTrackInfo, newItem: TagTrackInfo) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: TagTrackInfo, newItem: TagTrackInfo) =
                oldItem == newItem
        }
    }
}
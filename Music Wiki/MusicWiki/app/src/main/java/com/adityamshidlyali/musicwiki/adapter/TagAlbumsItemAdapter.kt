package com.adityamshidlyali.musicwiki.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.databinding.TagAlbumInfoItemBinding
import com.adityamshidlyali.musicwiki.model.tagmodel.TagAlbumInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Paging Adapter for displaying the list of Genre's albums
 */
class TagAlbumsItemAdapter(
    private val listener: OnAlbumItemClickListener
) : PagingDataAdapter<TagAlbumInfo, TagAlbumsItemAdapter.AlbumViewHolder>(ALBUM_COMPARATOR) {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding =
            TagAlbumInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d("TagAlbumsItemAdapter", "Hello")

        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = getItem(position)

        Log.d("TagAlbumsItemAdapter", "Hello")

        holder.bind(currentItem!!)
    }

    inner class AlbumViewHolder(
        private val binding: TagAlbumInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val albumItem = getItem(position)
                    if (albumItem != null) {
                        listener.onAlbumItemClick(albumItem)
                    }
                }
            }
        }

        fun bind(album: TagAlbumInfo) {
            binding.apply {
                Glide.with(itemView)
                    .load(album.image[3].imageLink)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_music_default)
                    .into(tagAlbumImage)

                tvTagAlbumItemAlbumName.text = album.name
                tvTagAlbumItemArtistName.text = album.artist.name
            }

            Log.d(TAG, "Hello")
        }
    }

    interface OnAlbumItemClickListener {
        fun onAlbumItemClick(album: TagAlbumInfo)
    }

    companion object {
        private val ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<TagAlbumInfo>() {

            override fun areItemsTheSame(oldItem: TagAlbumInfo, newItem: TagAlbumInfo) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: TagAlbumInfo, newItem: TagAlbumInfo) =
                oldItem == newItem
        }
    }
}
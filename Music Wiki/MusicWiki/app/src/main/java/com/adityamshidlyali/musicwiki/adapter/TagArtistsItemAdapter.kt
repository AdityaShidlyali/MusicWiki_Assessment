package com.adityamshidlyali.musicwiki.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.databinding.TagArtistInfoItemBinding
import com.adityamshidlyali.musicwiki.model.tagmodel.TagArtistInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Paging Adapter for displaying the list of Genre's albums
 */
class TagArtistsItemAdapter(
    private val listener: OnTrackItemClickListener
) : PagingDataAdapter<TagArtistInfo, TagArtistsItemAdapter.ArtistViewHolder>(ARTIST_COMPARATOR) {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding =
            TagArtistInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem!!)
    }

    inner class ArtistViewHolder(
        private val binding: TagArtistInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val trackItem = getItem(position)
                    if (trackItem != null) {
                        listener.onTrackItemClick(trackItem)
                    }
                }
            }
        }

        fun bind(artist: TagArtistInfo) {
            binding.apply {
                Glide.with(itemView)
                    .load(artist.image[3].imageLink)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_music_default)
                    .into(tagArtistImage)

                tvTagAlbumItemArtistName.text = artist.name
            }

            Log.d(TAG, "Hello")
        }
    }

    interface OnTrackItemClickListener {
        fun onTrackItemClick(artist: TagArtistInfo)
    }

    companion object {
        private val ARTIST_COMPARATOR = object : DiffUtil.ItemCallback<TagArtistInfo>() {

            override fun areItemsTheSame(oldItem: TagArtistInfo, newItem: TagArtistInfo) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: TagArtistInfo,
                newItem: TagArtistInfo
            ) = oldItem == newItem
        }
    }
}
package com.adityamshidlyali.musicwiki.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adityamshidlyali.musicwiki.R
import com.adityamshidlyali.musicwiki.databinding.ArtistTopTrackItemBinding
import com.adityamshidlyali.musicwiki.model.artistmodel.TopTrack
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Paging Adapter for displaying the list of Artist top tracks
 */
class ArtistTopTracksItemAdapter :
    PagingDataAdapter<TopTrack, ArtistTopTracksItemAdapter.TopTrackViewHolder>(TOP_TRACK_COMPARATOR) {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopTrackViewHolder {
        val binding =
            ArtistTopTrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d(TAG, "Hello")

        return TopTrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopTrackViewHolder, position: Int) {
        val currentItem = getItem(position)

        Log.d(TAG, "Hello")

        holder.bind(currentItem!!)
    }

    inner class TopTrackViewHolder(
        private val binding: ArtistTopTrackItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: TopTrack) {
            binding.apply {
                Glide.with(itemView)
                    .load(track.image[3].imageLink)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_music_default)
                    .into(artistTopTrackImage)

                tvArtistTopTrackName.text = track.name
            }

            Log.d(TAG, "Hello")
        }
    }

    companion object {
        private val TOP_TRACK_COMPARATOR = object : DiffUtil.ItemCallback<TopTrack>() {

            override fun areItemsTheSame(oldItem: TopTrack, newItem: TopTrack) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: TopTrack, newItem: TopTrack) =
                oldItem == newItem
        }
    }
}
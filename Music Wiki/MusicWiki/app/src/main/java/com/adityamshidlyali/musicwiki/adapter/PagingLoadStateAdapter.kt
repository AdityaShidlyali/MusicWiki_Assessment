package com.adityamshidlyali.musicwiki.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adityamshidlyali.musicwiki.databinding.PagingErrorStateBinding

/**
 * LoadStateAdapter for each of the PagingAdapter to handle the retry event
 */
class PagingLoadStateAdapter constructor(
    private val retry: () -> Unit
) : LoadStateAdapter<PagingLoadStateAdapter.PagingLoaderStateViewHolder>() {

    override fun onBindViewHolder(holder: PagingLoaderStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoaderStateViewHolder {
        return PagingLoaderStateViewHolder(
            PagingErrorStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), retry
        )
    }

    class PagingLoaderStateViewHolder(
        private val binding: PagingErrorStateBinding,
        retry: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.bRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                bRetry.isVisible = loadState !is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

}
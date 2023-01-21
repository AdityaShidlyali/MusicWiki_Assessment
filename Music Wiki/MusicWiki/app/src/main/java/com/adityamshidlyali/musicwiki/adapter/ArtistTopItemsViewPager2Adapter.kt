package com.adityamshidlyali.musicwiki.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adityamshidlyali.musicwiki.view.fragment.ArtistTopAlbumFragment
import com.adityamshidlyali.musicwiki.view.fragment.ArtistTopTracksFragment

/**
 * ViewPager2 Adapter for Artist's top tracks and top albums fragments
 */
class ArtistTopItemsViewPager2Adapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val bundle: Bundle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val TABS_COUNT = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val artistTopAlbumsFragment = ArtistTopAlbumFragment()
                artistTopAlbumsFragment.arguments = bundle
                artistTopAlbumsFragment
            }
            else -> {
                val artistTopTrackFragment = ArtistTopTracksFragment()
                artistTopTrackFragment.arguments = bundle
                artistTopTrackFragment
            }
        }
    }

    override fun getItemCount(): Int {
        return TABS_COUNT
    }
}
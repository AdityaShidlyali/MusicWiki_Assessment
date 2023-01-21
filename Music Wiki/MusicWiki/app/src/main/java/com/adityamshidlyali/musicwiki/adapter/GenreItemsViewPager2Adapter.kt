package com.adityamshidlyali.musicwiki.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.adityamshidlyali.musicwiki.view.fragment.AlbumsFragment
import com.adityamshidlyali.musicwiki.view.fragment.ArtistsFragment
import com.adityamshidlyali.musicwiki.view.fragment.TracksFragment

/**
 * ViewPager2 Adapter for Genre's albums, artists and tracks fragments
 */
class GenreItemsViewPager2Adapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val bundle: Bundle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val TABS_COUNT = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val albumsFragment = AlbumsFragment()
                albumsFragment.arguments = bundle
                albumsFragment
            }
            1 -> {
                val artistFragment = ArtistsFragment()
                artistFragment.arguments = bundle
                artistFragment
            }
            else -> {
                val tracksFragment = TracksFragment()
                tracksFragment.arguments = bundle
                tracksFragment
            }
        }
    }

    override fun getItemCount(): Int {
        return TABS_COUNT
    }
}
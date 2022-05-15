package com.example.twitchclient.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentSearchBinding
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.search.channels.ChannelsTabFragment
import com.example.twitchclient.ui.search.channels.ChannelsTabViewModel
import com.example.twitchclient.ui.search.games.GamesTabFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment(), SearchableFragment {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels {
        (activity as MainActivity).factory
    }

    private lateinit var vpAdapter: SearchTabsAdapter

    private var tabFragments: List<Fragment>? = null

    private val tabTitles = listOf("Каналы", "Игры")

    private var currentTabPosition = 0

    private var lastRequest = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSearchBinding.inflate(inflater, container, false).let {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }

    private fun initTabs() {
        tabFragments = listOf(
            ChannelsTabFragment(),
            GamesTabFragment()
        )
        vpAdapter = SearchTabsAdapter(requireActivity(), tabFragments!!)
        val tabLayout = requireActivity().findViewById<TabLayout>(R.id.tablayout)
        val vp = requireActivity().findViewById<ViewPager2>(R.id.search_viewpager)
        vp.adapter = vpAdapter
        TabLayoutMediator(tabLayout, vp) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    currentTabPosition = it.position
                    onQueryCall(lastRequest)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    override fun onQueryCall(request: String) {
        tabFragments?.let {
            if (request.isNotEmpty())
                (it[currentTabPosition] as SearchableTab).onSearch(request)
        }
        lastRequest = request
    }
}

private class SearchTabsAdapter(
    fa: FragmentActivity,
    private val tabFragments: List<Fragment>
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return tabFragments[position]
    }
}
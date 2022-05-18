package com.example.twitchclient.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentSearchBinding
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.search.channels.ChannelsTabFragment
import com.example.twitchclient.ui.search.games.GamesTabFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels {
        (activity as MainActivity).factory
    }

    private lateinit var vpAdapter: SearchTabsAdapter

    private var tabFragments = listOf<Fragment>(
        ChannelsTabFragment(),
        GamesTabFragment()
    )

    private val tabTitles = listOf("Каналы", "Игры")

    private var currentTabPosition = 0

    var queryTextChangedJob: Job? = null

    val queryDelay = 350L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSearchBinding.inflate(inflater, container, false).let {
        binding = it
        binding.toolbar.setupWithNavController(findNavController())
        initSearchView()
        binding.root
    }

    private fun initSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                lifecycleScope.launch {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = launch(Dispatchers.Main) {
                        delay(queryDelay)
                        if (p0 != null)
                            onRequest(p0)
                    }
                }
                return true
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }

    private fun initTabs() {
        vpAdapter = SearchTabsAdapter(requireActivity(), tabFragments)
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
                }
//                tab?.let {
//                    if (currentTabPosition != it.position) {
//                        currentTabPosition = it.position
//                        if (lastRequest.isNotEmpty())
//                            onRequest(lastRequest)
//                    }
//                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun onRequest(request: String) {
        tabFragments?.let {
            if (request.isNotEmpty())
                (it[currentTabPosition] as SearchableTab).search(request)
        }
        viewModel.lastRequest = request
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
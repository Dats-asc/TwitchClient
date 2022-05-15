package com.example.twitchclient.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ActivityMainBinding
import com.example.twitchclient.ui.followings.FollowingsFragment
import com.example.twitchclient.ui.navigation.NavOption
import com.example.twitchclient.ui.navigation.Navigator
import com.example.twitchclient.ui.search.SearchFragment
import com.example.twitchclient.utils.MyViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), Navigator {

    companion object {
        private const val USER_PREFERENCES = "USER_PREFERENCES"
        private const val USER_ACCESS_TOKEN_VALUE = "USER_ACCESS_TOKEN_VALUE"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferences: SharedPreferences

    private var navOption = NavOption.OPTION_DEFAULT

    private lateinit var navController: NavController

    private val destinationListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.streamFragment -> hideBtmNavAndToolbar()
                else -> showBtmNavAndToolbar()
            }
        }

    @Inject
    lateinit var factory: MyViewModelFactory

    private val viewModel: MainViewModel by viewModels { factory }

    private var currentNavigationItem = R.id.navigation_followings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        putAccessToken("tthzw65o8p57eb80y0jwkr6lfcg3rt")
    }

    override fun onStart() {
        super.onStart()
        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchItem?.actionView = SearchView(this, null, R.style.SearchView).also {
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.orEmpty().isNotEmpty()){
                        lifecycleScope.launch {
                            val searchFragment =
                                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)?.childFragmentManager?.let {
                                    it.fragments[0] as SearchFragment
                                }
                            searchFragment?.onQueryCall(newText.orEmpty())
                        }
                    }
                    return true
                }

            })
        }
        val searchView: SearchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item)
    }


    private fun init() {
        preferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        setupToolbar()
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment_container)
        navController.addOnDestinationChangedListener(destinationListener)
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navController
        )
        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
            AppBarConfiguration(
                setOf(
                    R.id.navigation_followings,
                    R.id.navigation_popular,
                    R.id.navigation_games
                )
            )
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun hideBtmNavAndToolbar() {
        binding.toolbar.visibility = View.GONE
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBtmNavAndToolbar() {
        binding.toolbar.visibility = View.VISIBLE
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun updateToolbar() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }


        when (navOption) {
            NavOption.OPTION_HIDE_TOOLBAR_AND_BOTTOM_NAV_VIEW -> {
                binding.bottomNavigationView.visibility = View.GONE
                supportActionBar?.hide()
            }
            NavOption.OPTION_DEFAULT -> {
                binding.bottomNavigationView.visibility = View.VISIBLE
                supportActionBar?.show()
            }
        }
    }

    override fun pushFragment(bundle: Bundle?, fragId: Int) {
        val options = NavOptions.Builder()
            .setLaunchSingleTop(false)
            .build()
        navController.navigate(fragId, bundle, options)

    }

    override fun backToStart() {
        navOption = NavOption.OPTION_DEFAULT
        updateToolbar()
        with(supportFragmentManager) {
            popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            beginTransaction().run {
                replace(R.id.nav_host_fragment_container, FollowingsFragment())
                commit()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.nav_host_fragment_container, fragment)
            commit()
        }
    }

    fun putAccessToken(token: String) {
        preferences.edit()
            .putString(USER_ACCESS_TOKEN_VALUE, token)
            .apply()
    }

    fun getAccessToken(): String? = preferences.getString(USER_ACCESS_TOKEN_VALUE, null)
}
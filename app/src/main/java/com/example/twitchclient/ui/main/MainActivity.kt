package com.example.twitchclient.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.twitchclient.MyApp
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ActivityMainBinding
import com.example.twitchclient.ui.auth.AuthFragment
import com.example.twitchclient.ui.followings.FollowingsFragment
import com.example.twitchclient.ui.games.GamesFragment
import com.example.twitchclient.ui.navigation.NavOption
import com.example.twitchclient.ui.navigation.Navigator
import com.example.twitchclient.ui.popular.PopularFragment
import com.example.twitchclient.utils.ViewModelFactory
import javax.inject.Inject

val USER_PREFERENCES = "USER_PREFERENCES"

class MainActivity : AppCompatActivity(), Navigator {

    companion object {
        private const val USER_PREFERENCES = "USER_PREFERENCES"
        private const val USER_ACCESS_TOKEN_VALUE = "USER_ACCESS_TOKEN_VALUE"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferences: SharedPreferences

    private var navOption = NavOption.OPTION_DEFAULT

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels {
        factory
    }

    private var currentNavigationItem = R.id.navigation_followings

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateToolbar()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        putAccessToken("tthzw65o8p57eb80y0jwkr6lfcg3rt")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun init() {
        preferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        setupToolbar()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        with(binding.toolbar) {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_search -> {
                        true
                    }

                    else -> false
                }
            }

            setNavigationOnClickListener {
                goBack()
            }
        }
    }

    private fun setupBottomNavigation() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
        supportFragmentManager.beginTransaction().run {
            add(R.id.nav_host_fragment_container, FollowingsFragment())
            commit()
        }
        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbar()
        }

        binding.bottomNavigationView.selectedItemId = R.id.navigation_followings

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId != currentNavigationItem) {
                currentNavigationItem = item.itemId
                onNavigationItemSelected(currentNavigationItem)
                true
            } else
                false
        }
    }

    private fun onNavigationItemSelected(itemId: Int) {
        when (itemId) {
            R.id.navigation_games -> {
                replaceFragment(GamesFragment())
            }
            R.id.navigation_popular -> {
                replaceFragment(PopularFragment())
            }
            R.id.navigation_followings -> {
                replaceFragment(FollowingsFragment())
            }
        }
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

    override fun pushFragment(fragment: Fragment, navOption: NavOption?) {
        this.navOption = navOption ?: NavOption.OPTION_DEFAULT
        supportFragmentManager.beginTransaction().run {
            setCustomAnimations(
                R.anim.test1,
                R.anim.test2,
                R.anim.test1,
                R.anim.test2
            )
            addToBackStack(null)
            replace(R.id.nav_host_fragment_container, fragment)
            commit()
        }
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

    override fun goBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navOption = NavOption.OPTION_DEFAULT
        updateToolbar()
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

    fun getAccessToken(): String? = preferences.getString(USER_ACCESS_TOKEN_VALUE, "")
}
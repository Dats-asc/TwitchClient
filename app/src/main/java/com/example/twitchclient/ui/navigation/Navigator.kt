package com.example.twitchclient.ui.navigation

import androidx.fragment.app.Fragment

fun Fragment.navigator() : Navigator{
    return requireActivity() as Navigator
}

interface Navigator {

    fun pushFragment(fragment: Fragment, navOption: NavOption?)

    fun replaceFragment(fragment: Fragment)

    fun backToStart()

    fun goBack()

}

public enum class NavOption{
    OPTION_DEFAULT,
    OPTION_HIDE_TOOLBAR_AND_BOTTOM_NAV_VIEW
}
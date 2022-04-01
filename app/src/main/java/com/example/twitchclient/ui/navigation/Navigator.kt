package com.example.twitchclient.ui.navigation

import androidx.fragment.app.Fragment

fun Fragment.navigator() : Navigator{
    return requireActivity() as Navigator
}

interface Navigator {

    fun pushFragment(fragment: Fragment)

    fun replaceFragment(fragment: Fragment)

    fun backToStart()

    fun goBack()

}
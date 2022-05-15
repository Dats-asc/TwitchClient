package com.example.twitchclient.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment

fun Fragment.navigator() : Navigator{
    return requireActivity() as Navigator
}

interface Navigator {

    fun pushFragment(bundle: Bundle?, fragId: Int)

    fun replaceFragment(fragment: Fragment)

    fun backToStart()

    fun goBack()

}

public enum class NavOption{
    OPTION_DEFAULT,
    OPTION_HIDE_TOOLBAR_AND_BOTTOM_NAV_VIEW
}
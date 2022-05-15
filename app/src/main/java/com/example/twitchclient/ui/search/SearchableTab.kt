package com.example.twitchclient.ui.search

import android.app.Activity
import androidx.fragment.app.Fragment

fun Fragment.onSearch() : SearchableTab{
    return Fragment() as SearchableTab
}

interface SearchableTab {

    fun onSearch(request: String)
}
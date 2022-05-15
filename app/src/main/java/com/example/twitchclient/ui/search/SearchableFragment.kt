package com.example.twitchclient.ui.search

import android.app.Activity
import androidx.fragment.app.Fragment

fun Activity.onQueryCall(request: String) : SearchableFragment{
    return Fragment() as SearchableFragment
}

interface SearchableFragment {

    fun onQueryCall(request: String)
}
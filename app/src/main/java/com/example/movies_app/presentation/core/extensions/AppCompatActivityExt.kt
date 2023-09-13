package com.example.movies_app.presentation.core.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import timber.log.Timber

inline fun <reified T : Fragment> AppCompatActivity.addFragment(
    @IdRes containerViewId: Int,
    tag: String? = null,
    args: Bundle? = null,
    addToBackStack: Boolean = true
) {
    val className = T::class.java.simpleName
    Timber.d("D/Navigate to $className")
    supportFragmentManager.commit {
        add<T>(containerViewId, tag, args)
        if (addToBackStack) addToBackStack("${className}Transaction")
    }
}
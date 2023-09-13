package com.example.movies_app.presentation.core.extensions

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.movies_app.R
import timber.log.Timber

inline fun <reified T : Fragment> Fragment.navigateTo(
    args: Bundle? = null,
    tag: String? = null,
    addToBackStack: Boolean = true
) {
    navigateTo<T>(R.id.container, args, tag, addToBackStack)
}

inline fun <reified T : Fragment> Fragment.navigateTo(
    @IdRes containerViewId: Int,
    args: Bundle? = null,
    tag: String? = null,
    addToBackStack: Boolean = true
) {
    val className = T::class.java.simpleName
    Timber.d("D/Navigate to $className")
    parentFragmentManager.commit {
        replace<T>(containerViewId, tag, args)
        setReorderingAllowed(true)
        if (addToBackStack) addToBackStack("${className}Transaction")
    }
}

fun Fragment.navigateBack() {
    Timber.d("D/Navigate Back")
    parentFragmentManager.popBackStackImmediate()
}

fun addTopViewInsets(topView: View) {
    InsetsHelper.addInsets(topView, true)
}

object InsetsHelper {
    fun addInsets(v: View, top: Boolean) {
        ViewCompat.setOnApplyWindowInsetsListener(v) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                if (top) {
                    topMargin = insets.top
                } else {
                    bottomMargin = insets.bottom
                }
                leftMargin = insets.left
                rightMargin = insets.right
            }
            WindowInsetsCompat.CONSUMED
        }
    }
}
package com.example.movies_app.presentation.core.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

@Suppress("unused")
fun Context.toast(message: CharSequence, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, length).show()
}

fun Context.hideSoftKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
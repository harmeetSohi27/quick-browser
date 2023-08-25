package com.ultivic.quickbrowser.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Patterns
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


val String.isValidUrl: Boolean
    get() {
        return Patterns.WEB_URL.matcher(this).matches()
    }


fun Fragment.onBack(callback: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            callback.invoke()
        }
    })
}

val Bitmap.toByteArray: ByteArray
    get() = ByteArrayOutputStream().apply {
        this@toByteArray.compress(Bitmap.CompressFormat.PNG, 100, this)
    }.toByteArray()

val ByteArray.toBitmap: Bitmap
    get() {
        return BitmapFactory.decodeByteArray(this, 0, size)
    }
val Long.toFormatDate: String
    get() {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        return formatter.format(Date(this))
    }

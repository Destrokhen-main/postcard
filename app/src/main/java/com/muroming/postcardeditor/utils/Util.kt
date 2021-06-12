package com.muroming.postcardeditor.utils

import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import com.muroming.postcardeditor.ui.views.textaddingview.TextViewStyle

fun Float.toSp() = this / Resources.getSystem().displayMetrics.scaledDensity

fun Float.spToPx() = this * Resources.getSystem().displayMetrics.scaledDensity

fun View.setSize(newWidth: Int = width, newHeight: Int = height) {
    layoutParams = layoutParams.apply {
        width = newWidth
        height = newHeight
    }
}

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (!isVisible) View.GONE else View.VISIBLE
}

fun Uri.toBitmap(resolver: ContentResolver): Bitmap =
    MediaStore.Images.Media.getBitmap(resolver, this)

fun GradientDrawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    setStroke(1, Color.BLACK)
    isFilterBitmap = true
    draw(canvas)
    return bitmap
}

fun TextView.applyStyle(style: TextViewStyle) {
    letterSpacing = style.letterSpacing
    setLineSpacing(0f, style.linesSpacingMult)
}
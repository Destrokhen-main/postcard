package com.muroming.postcardeditor.ui.views.editorview

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class PicassoPhotoTarget(
    private val onBitmapReady: (Bitmap) -> Unit
) : Target {
    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        bitmap?.let(onBitmapReady)
    }
}
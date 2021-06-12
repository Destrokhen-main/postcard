package com.muroming.postcardeditor.listeners

import android.net.Uri

interface OnCropFinishedListener {
    fun onImageCropped(uri: Uri)
    fun onCropCanceled()
}
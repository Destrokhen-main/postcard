package com.muroming.postcardeditor.listeners

import android.net.Uri

interface OnImagePickedListener {
    fun onImagePicked(imageUri: Uri)
}
package com.muroming.postcardeditor.data.dto

import android.graphics.drawable.GradientDrawable
import android.net.Uri

sealed class UserPicture

data class UriPicture(
    val uri: Uri
) : UserPicture()

data class DrawablePicture(
    val gradientDrawable: GradientDrawable,
    val tint: Int? = null
) : UserPicture()
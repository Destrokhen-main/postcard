package com.muroming.postcardeditor.ui.views.editorview

import ja.burhanrashid52.photoeditor.PhotoEditor

class PhotoSaveListener (
    private val onSaved: (String) -> Unit,
    private val onFailure: () -> Unit = {}
) : PhotoEditor.OnSaveListener {
    override fun onSuccess(imagePath: String) = onSaved(imagePath)

    override fun onFailure(exception: Exception) = onFailure()
}
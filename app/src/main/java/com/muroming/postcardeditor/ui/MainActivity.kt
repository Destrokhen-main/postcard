package com.muroming.postcardeditor.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.muroming.postcardeditor.R
import com.muroming.postcardeditor.ui.fragments.PhotoEditorFragment
import com.yalantis.ucrop.UCrop

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var editorFragment: PhotoEditorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editorFragment = PhotoEditorFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.flMainHolder, editorFragment)
            .commit()
    }

    override fun onBackPressed() {
         val isBackPressHandled = editorFragment.onBackPressed()

        if(!isBackPressHandled) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        handleCropResult(requestCode, resultCode, data)
        handleImagePickResult(requestCode, resultCode, data)
    }

    private fun handleCropResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
            UCrop.getOutput(data)?.let(editorFragment::onImageCropped)
        }
        if (resultCode == Activity.RESULT_CANCELED && requestCode == UCrop.REQUEST_CROP) {
            editorFragment.onCropCanceled()
        }
    }

    private fun handleImagePickResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PhotoEditorFragment.RESULT_PICK_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let(editorFragment::onImagePicked)
        }
    }
}
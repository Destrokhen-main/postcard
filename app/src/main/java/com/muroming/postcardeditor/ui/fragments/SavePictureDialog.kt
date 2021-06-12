package com.muroming.postcardeditor.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.muroming.postcardeditor.R

class SavePictureDialog(
    private val onSavePicture: () -> Unit,
    private val onNotSavingPicture: () -> Unit = {}
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setPositiveButton(R.string.save_picture) { _, _ ->
                onSavePicture()
                dismiss()
            }
            .setNegativeButton(R.string.dont_save_picture) { _, _ ->
                onNotSavingPicture()
                dismiss()
            }
            .setNeutralButton(R.string.dismiss_dialog) { _, _ ->
                dismiss()
            }
            .setMessage(R.string.editor_closing_warning)
            .create()
    }

    companion object {
        val DIALOG_TAG = SavePictureDialog::class.java.canonicalName
    }
}
package com.muroming.postcardeditor.ui.views.editorview

import android.view.View
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.ViewType

class DrawingListener(
    private val onDrawingStated: () -> Unit,
    private val onDrawingStopped: () -> Unit,
    private val applyChanges: () -> Unit
) : OnPhotoEditorListener {
    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {}

    override fun onStartViewChangeListener(viewType: ViewType?) {
        if (viewType == ViewType.BRUSH_DRAWING) {
            onDrawingStated()
        }
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {}

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        if (viewType == ViewType.BRUSH_DRAWING && numberOfAddedViews > 0) {
            applyChanges()
        }
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        if (viewType == ViewType.BRUSH_DRAWING) {
            onDrawingStopped()
        }
    }
}
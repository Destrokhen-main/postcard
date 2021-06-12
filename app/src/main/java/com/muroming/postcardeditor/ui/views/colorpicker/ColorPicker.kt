package com.muroming.postcardeditor.ui.views.colorpicker

import android.content.Context
import com.skydoves.colorpickerview.ColorPickerDialog

class ColorPicker(
    context: Context,
    onColorSelected: (Int) -> Unit
) {
    private val dialog =
        ColorPickerDialog.Builder(context)
            .setTitle("Выберите цвет")
            .setPositiveButton("Выбрать", ColorListener(onColorSelected) )
            .setNegativeButton("Закрыть") { d, _ -> d.dismiss() }

    fun show() {
        dialog.show()
    }
}
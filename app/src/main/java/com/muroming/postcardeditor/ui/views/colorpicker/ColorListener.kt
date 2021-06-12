package com.muroming.postcardeditor.ui.views.colorpicker

import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class ColorListener(
    private val onColorSelected: (Int) -> Unit
) : ColorEnvelopeListener {
    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
        if (fromUser) {
            envelope?.color?.let(onColorSelected)
        }
    }
}
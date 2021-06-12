package com.muroming.postcardeditor.ui.views.textaddingview

import android.widget.SeekBar

class SeekBarListener(
    private val min: Float,
    private val max: Float,
    private val onProgress: (Float) -> Unit
) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (!fromUser) return

        val newValue = min + (progress / 100f) * (max - min)
        onProgress(newValue)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}
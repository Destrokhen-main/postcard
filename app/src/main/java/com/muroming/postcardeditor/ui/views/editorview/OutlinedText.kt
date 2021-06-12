package com.muroming.postcardeditor.ui.views.editorview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.muroming.postcardeditor.utils.spToPx

class OutlinedText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    var strokeColor: Int = 0
    private var _strokeWidth: Float = 0.toFloat()
    private var isDrawing: Boolean = false

    init {
        strokeColor = currentTextColor
        _strokeWidth = DEFAULT_STROKE_WIDTH.toFloat()
        setStrokeWidth(_strokeWidth)
    }

    override fun invalidate() {
        if (isDrawing) return
        super.invalidate()
    }

    private fun setStrokeWidth(width: Float) {
        _strokeWidth = width.spToPx()
    }

    fun setStrokeWidth(unit: Int, width: Float) {
        _strokeWidth = TypedValue.applyDimension(
            unit, width, context.resources.displayMetrics
        )
    }

    // overridden methods
    override fun onDraw(canvas: Canvas) {
        if (_strokeWidth > 0) {
            isDrawing = true
            //set paint to fill mode
            val p = paint
            p.style = Paint.Style.FILL
            //draw the fill part of text
            super.onDraw(canvas)
            //save the text color
            val currentTextColor = currentTextColor
            //set paint to stroke mode and specify
            //stroke color and width
            p.style = Paint.Style.STROKE
            p.strokeWidth = _strokeWidth
            setTextColor(strokeColor)
            //draw text stroke
            super.onDraw(canvas)
            //revert the color back to the one
            //initially specified
            setTextColor(currentTextColor)
            isDrawing = false
        } else {
            super.onDraw(canvas)
        }
    }

    companion object {
        private const val DEFAULT_STROKE_WIDTH = 0
    }
}
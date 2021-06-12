package com.muroming.postcardeditor.ui.views.textaddingview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.muroming.postcardeditor.R
import com.muroming.postcardeditor.listeners.OnKeyboardShownListener
import com.muroming.postcardeditor.ui.views.colorpicker.ColorPicker
import com.muroming.postcardeditor.ui.views.editorview.OutlinedText
import com.muroming.postcardeditor.utils.applyStyle
import com.muroming.postcardeditor.utils.setVisibility
import com.muroming.postcardeditor.utils.toSp
import kotlinx.android.synthetic.main.text_adding_view.view.*

class TextAddingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var keyboardListener: OnKeyboardShownListener

    private var isTextBold = false
    private var isTextItalic = false
    private var isTextOutlined = false
    private var selectedOutlineColor = ContextCompat.getColor(context, R.color.red)

    private val minScaleShadowRadius = 1f
    private val maxScaleShadowRadius = 3f

    private var currentScaleShadowRadius: Float = 1f

    private val minTextSize = resources.getDimensionPixelSize(R.dimen.min_text_size).toFloat()
    private val maxTextSize = resources.getDimensionPixelSize(R.dimen.max_text_size).toFloat()

    private val minTextHeightScale = 1f
    private val maxTextHeightScale = 2f

    private val minTextSpacing = 0f
    private val maxTextSpacing = 1f

    private var currentTypeface: Int? = null
    private var editedTextHolder: ViewGroup? = null

    private val inputMethodManager: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    private val editorFonts = listOf(
        "Обычный" to null,
        "Aqum" to R.font.aqum,
        "AGCrownStyle Roman" to R.font.agcrownstyleroman,
        "ANVYL" to R.font.anvyl,
        "Atlas Deco A" to R.font.atlas_deco_a,
        "Bulky" to R.font.bulky,
        "Cheque-Black" to R.font.chequeblack,
        "Cheque-Regular" to R.font.chequeregular,
        "Cinematografica-Heavy-trial" to R.font.cinematograficaheavytrial,
        "Codec-Cold-Heavy-Italic-trial" to R.font.codeccoldheavyitalictrial,
        "Codec-Warm-Regular-trial" to R.font.codecwarmregulartrial,
        "Copyist" to R.font.copyist,
        "Etude" to R.font.etude,
        "Intro" to R.font.intro,
        "Marianna" to R.font.marianna,
        "Monika Script" to R.font.monika_script,
        "Nadejda Bold" to R.font.nadejda_bold,
        "PFScandalPro-Black" to R.font.pfscandalpro_black,
        "Rapier Cyr" to R.font.rapiercyr,
        "ReSquare Bold" to R.font.resquare_bold,
        "Taverna" to R.font.taverna,
        "TRF" to R.font.trf,
        "Valet" to R.font.valet,
        "ZingRustDemo-Base" to R.font.zingrustdemo_base,
        "ZingSansRustLDemo-Base" to R.font.zingsansrustldemo_base
    )
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    lateinit var textListener: TextAddingViewListener

    init {
        LayoutInflater.from(context).inflate(R.layout.text_adding_view, this, true)
        initTextControls()
        setupFontsAdapter()
    }

    private fun setupFontsAdapter() {
        spinnerAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            editorFonts.map(Pair<String, Int?>::first)
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            vTypefacesSpinner.adapter = this
        }

        vTypefacesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentTypeface = editorFonts[position].second
                updateTextTypeface()
            }
        }
    }

    private fun initTextControls() {
        ivTextToLeft.setOnClickListener { etTextInput.gravity = Gravity.LEFT }
        ivTextToRight.setOnClickListener { etTextInput.gravity = Gravity.RIGHT }
        ivTextToCenter.setOnClickListener { etTextInput.gravity = Gravity.CENTER }

        ivBoldText.setOnClickListener { setInputTextBold() }
        ivItalicText.setOnClickListener { setInputTextItalic() }
        ivOutlineText.setOnClickListener {
            isTextOutlined = !isTextOutlined
            setInputTextOutlined()
        }
        ivOutlineText.setOnLongClickListener { selectOutlineColor(); true; }

        vInputTextBackground.setOnClickListener {
            textListener.onTextReady(
                etTextInput.text.toString(),
                etTextInput.gravity,
                etTextInput.textSize.toSp(),
                etTextInput.currentTextColor,
                if (isTextOutlined) selectedOutlineColor else null,
                if (isTextOutlined) currentScaleShadowRadius else 1f,
                etTextInput.typeface,
                TextViewStyle(
                    currentTypeface,
                    isTextBold,
                    isTextItalic,
                    etTextInput.lineSpacingMultiplier,
                    etTextInput.letterSpacing,
                    (etTextInput.tag as? Float) ?: 0f
                )
            )
            etTextInput.tag = null
        }
        vTextSizeSlider.setOnSeekBarChangeListener(
            SeekBarListener(
                minTextSize,
                maxTextSize
            ) { value ->
                etTextInput.textSize = value
            })
        vSpacingSlider.setOnSeekBarChangeListener(
            SeekBarListener(
                minTextSpacing,
                maxTextSpacing
            ) { value ->
                etTextInput.letterSpacing = value
            }
        )

        vTextHeightSlider.setOnSeekBarChangeListener(
            SeekBarListener(
                minTextHeightScale,
                maxTextHeightScale
            ) { value ->
                etTextInput.setLineSpacing(0f, value)
            }
        )

        vShadowScaleSlider.setOnSeekBarChangeListener(
            SeekBarListener(
                minScaleShadowRadius,
                maxScaleShadowRadius
            ) {
                value ->
                run {
                    Log.d("TextViewLog", "setTextOutlineListener $currentScaleShadowRadius")
                    currentScaleShadowRadius = value
                    etTextInput.setShadowLayer(
                        etTextInput.textSize / 20 * value,
                        0f, 0f,
                        selectedOutlineColor
                    )
                }

            }
        )
    }

    private fun selectOutlineColor() {
        ColorPicker(context) { newColor ->
            selectedOutlineColor = newColor
            setInputTextOutlined()
        }.show()
    }

    private fun setInputTextBold() {
        isTextBold = !isTextBold
        updateTextTypeface()
    }

    private fun setInputTextItalic() {
        isTextItalic = !isTextItalic
        updateTextTypeface()
    }

    private fun updateTextTypeface() {
        etTextInput.setTypeface(
            currentTypeface?.let { ResourcesCompat.getFont(context, it) },
            getTextStyle()
        )
    }

    private fun setInputTextOutlined() {
        if (isTextOutlined) {
            Log.d("TextViewLog", "setTextOutline $currentScaleShadowRadius")
            etTextInput.setShadowLayer(
                etTextInput.textSize / 20 * currentScaleShadowRadius,
                0f, 0f,
                selectedOutlineColor
            )
        } else {
            etTextInput.setShadowLayer(0f, 0f, 0f, 0)
        }
    }

    private fun getTextStyle(): Int = if (isTextBold) {
        if (isTextItalic) Typeface.BOLD_ITALIC else Typeface.BOLD
    } else {
        if (isTextItalic) Typeface.ITALIC else Typeface.NORMAL
    }

    fun setInputTextGroupVisibility(isVisible: Boolean) {
        setVisibility(isVisible)
        etTextInput.apply {
            resetInputText()
            manageFocusWithKeyboard(isVisible)
        }
        vTypefacesSpinner.setSelection(0)
        intervalsGroup.setVisibility(false)
    }

    fun setTextColor(color: Int) {
        etTextInput.setTextColor(color)
    }

    fun onBackPressed() {
        if (editedTextHolder == null) {
            setInputTextGroupVisibility(false)
        } else {
            textListener.onTextReady(
                etTextInput.text.toString(),
                etTextInput.gravity,
                etTextInput.textSize.toSp(),
                etTextInput.currentTextColor,
                if (isTextOutlined) selectedOutlineColor else null,
                if (isTextOutlined) currentScaleShadowRadius else 1f,
                etTextInput.typeface,
                TextViewStyle(
                    currentTypeface,
                    isTextBold,
                    isTextItalic,
                    etTextInput.lineSpacingMultiplier,
                    etTextInput.letterSpacing,
                    etTextInput.tag as Float
                )
            )
        }
        etTextInput.tag = null
    }

    private fun EditText.manageFocusWithKeyboard(shouldRequestFocus: Boolean) {
        handler?.postDelayed(
            {
                if (shouldRequestFocus) {
                    if (requestFocus()) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                        keyboardListener.onKeyboardVisible()
                    }
                } else {
                    clearFocus()
                    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
                    keyboardListener.onKeyboardHidden()
                }
            },
            EDITTEXT_FOCUS_REQUEST_DELAY
        )
    }

    private fun EditText.resetInputText() {
        setText("")
        textSize = minTextSize
        setLineSpacing(0f, 1f)
        textSize = resources.getDimensionPixelSize(R.dimen.default_text_size).toFloat()
        letterSpacing = 0f
        gravity = Gravity.START
        etTextInput.setTextColor(ContextCompat.getColor(context, R.color.black))
        etTextInput.setShadowLayer(0f, 0f, 0f, 0)
        currentTypeface = null
        editedTextHolder = null
        isTextBold = false
        isTextItalic = false
        isTextOutlined = false
        setTypeface(null, Typeface.NORMAL)
        setSlidersValues()
    }

    fun editText(textHolder: ViewGroup, textStyle: TextViewStyle) {
        setInputTextGroupVisibility(true)
        val textView = textHolder.children.first() as TextView
        editedTextHolder = textHolder

        etTextInput.apply {
            setText(textView.text)
            setTextColor(textView.currentTextColor)
            textSize = textView.textSize.toSp()
            gravity = textView.gravity
            applyStyle(textStyle)
            tag = (textHolder.parent as View).rotation
            if (textView is OutlinedText) {
                isTextOutlined = true
                selectedOutlineColor = textView.strokeColor
                Log.d("TextViewLog", "setTextOutlineApply $currentScaleShadowRadius")
                etTextInput.setShadowLayer(
                    etTextInput.textSize / 20 * currentScaleShadowRadius,
                    0f, 0f,
                    selectedOutlineColor
                )
            }
        }
        currentTypeface = textStyle.typeface
        isTextBold = textStyle.isBold
        isTextItalic = textStyle.isItalic

        val fontIndex = editorFonts.indexOfFirst { (_, font) ->
            currentTypeface == font
        }
        vTypefacesSpinner.setSelection(fontIndex)
        updateTextTypeface()
        setSlidersValues()
    }

    private fun setSlidersValues() {
        val textSizeValue =
            (etTextInput.textSize.toSp() - minTextSize) / (maxTextSize - minTextSize) * 100
        val spacingValue =
            (etTextInput.letterSpacing - minTextSpacing) / (maxTextSpacing - minTextSpacing) * 100
        val scaleYValue =
            (etTextInput.lineSpacingMultiplier - minTextHeightScale) / (maxTextHeightScale - minTextHeightScale) * 100

        vTextSizeSlider.progress = textSizeValue.toInt()
        vSpacingSlider.progress = spacingValue.toInt()
        vTextHeightSlider.progress = scaleYValue.toInt()
    }

    fun onIntervalsClicked() {
        if (isVisible) {
            intervalsGroup.setVisibility(intervalsGroup.isVisible.not())
        }
    }

    companion object {
        private const val EDITTEXT_FOCUS_REQUEST_DELAY = 30L
    }
}
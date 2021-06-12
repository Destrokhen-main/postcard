package com.muroming.postcardeditor.ui.fragments

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muroming.postcardeditor.R
import com.muroming.postcardeditor.data.PresetsRepository
import com.muroming.postcardeditor.data.dto.DrawablePicture
import com.muroming.postcardeditor.data.dto.UserPicture
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class PhotoEditorViewModel : ViewModel() {
    private lateinit var presetsRepository: PresetsRepository

    private val presets = MutableLiveData<List<UserPicture>>()
    private val userPictures = MutableLiveData<List<UserPicture>>()
    private val themedPresets = MutableLiveData<CardPresets>(CardPresets.Loading)
    private val editorState = MutableLiveData(EditorState.THEMED_PRESETS)

    private var presetsState = EditorState.FRAME_PRESETS

    fun observeUserPictures(): LiveData<List<UserPicture>> = userPictures
    fun observeThemedPresets(): LiveData<CardPresets> = themedPresets
    fun observeEditorState(): LiveData<EditorState> = editorState
    fun observePresets(): LiveData<List<UserPicture>> = presets

    fun getEditorState() = editorState.value

    fun initPresetsRepo(directory: File) {
        presetsRepository = PresetsRepository(directory)
    }

    fun loadUserPictures() {
        presetsRepository.loadUserPictures().let {
            userPictures.value = it
            presets.takeIf {
                presetsState == EditorState.USER_PICTURES ||
                        editorState.value == EditorState.USER_PICTURES
            }?.value = it
        }
    }

    fun reloadPresets() {
        viewModelScope.launch {
            themedPresets.value = CardPresets.Loading
            try {
                themedPresets.value = CardPresets.Loaded(presetsRepository.loadPresets())
            } catch (e: Exception) {
                themedPresets.value = CardPresets.Error
            }
        }
    }

    private fun reloadFrames() {
        viewModelScope.launch {
            presets.value = emptyList()
            try {
                presets.value = presetsRepository.loadFrames()
            } catch (ignored: Exception) {
            }
        }
    }

    private fun loadFills(resources: Resources) {
        val fills = listOf(
            R.drawable.fill_blue,
            R.drawable.fill_deep_orange,
            R.drawable.fill_green,
            R.drawable.fill_indigo,
            R.drawable.fill_orange,
            R.drawable.fill_pink,
            R.drawable.fill_purple,
            R.drawable.fill_red,
            R.drawable.fill_teal,
            R.drawable.fill_yellow
        ).shuffled()
            .map { resId ->
                DrawablePicture(
                    resources.getDrawable(resId, null) as GradientDrawable
                )
            }
        viewModelScope.launch {
            val loadedFills = presetsRepository
                .loadFillColors()
                .map { color ->
                    Log.d("PEModel", color)
                    DrawablePicture(
                        resources.getDrawable(R.drawable.fill_blue, null) as GradientDrawable,
                        Color.parseColor(color)
                    )
                }

            presets.value = fills + loadedFills
            loadedFills.forEach {
                Log.d("PEModel", "inside ${it.tint}")
            }
            Log.d("PEModel", "inside ${(fills + loadedFills).size}")
        }
        Log.d("PEModel", "outside ${fills.size}")
        presets.value = fills
    }

    fun onPresetClicked() {
        editorState.value?.takeIf { it != EditorState.EDITING }?.let { presetsState = it }
        editorState.value = EditorState.EDITING
        Log.d("PEModel", presetsState.toString())
    }

    fun onSavingComplete() {
        Log.d("PEModel", "$presetsState ${editorState.value}")
        //editorState.value = presetsState
        editorState.value = EditorState.THEMED_PRESETS
        presetsState = EditorState.THEMED_PRESETS
        //loadUserPictures()
        reloadPresets()
    }

    fun onFinishedEditing() {
        editorState.value = presetsState
        editorState.value = EditorState.THEMED_PRESETS
        presetsState = EditorState.THEMED_PRESETS
        reloadPresets()
    }

    fun generateFilePath(directory: File): String {
        return "${directory.path}/myPicture${UUID.randomUUID()}.png"
    }

    fun onFillClicked(resources: Resources) {
        editorState.value = EditorState.FILL_PRESETS
        loadFills(resources)
    }

    fun onPresetsClicked() {
        editorState.value = EditorState.FRAME_PRESETS
        reloadPresets()
    }

    fun onFramesClicked() {
        editorState.value = EditorState.FRAME_PRESETS
        reloadFrames()
    }

    fun onAllPicturesClicked() {
        editorState.value = EditorState.USER_PICTURES
    }

    fun onOpenThemesClicked(theme: String) {
        presets.value = (themedPresets.value as CardPresets.Loaded)
            .presets
            .first { it.title == theme }.pictures
        editorState.value = EditorState.FRAME_PRESETS
        Log.d("PEModel", "onOpenThemeCl ${presets.toString()} ${editorState.value}")
    }

    fun showThemedPresets() {
        editorState.value = EditorState.THEMED_PRESETS
    }
}
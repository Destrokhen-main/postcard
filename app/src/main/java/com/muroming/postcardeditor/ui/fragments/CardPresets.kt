package com.muroming.postcardeditor.ui.fragments

import com.muroming.postcardeditor.ui.themedadapter.ThemedUserPictures

sealed class CardPresets(val presets: List<ThemedUserPictures>) {
    object Loading : CardPresets(emptyList())
    object Error : CardPresets(emptyList())

    class Loaded(presets: List<ThemedUserPictures>) : CardPresets(presets)
}
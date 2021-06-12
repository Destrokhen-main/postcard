package com.muroming.postcardeditor.data

import androidx.core.net.toUri
import com.muroming.postcardeditor.data.dto.UriPicture
import com.muroming.postcardeditor.data.dto.UserPicture
import com.muroming.postcardeditor.ui.themedadapter.ThemedUserPictures
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class PresetsRepository(
    private val directory: File
) {
    private val presets = Retrofit.Builder()
        .baseUrl(PRESETS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PresetsApi::class.java)

    suspend fun loadPresets(): List<ThemedUserPictures> {
        return presets.loadPresets()
            .filter { it.category != FRAMES_CATEGORY }
            .groupBy { it.category }
            .map { (theme, pics) ->
                ThemedUserPictures(theme, pics.map { UriPicture(it.uri.toUri()) })
            }
    }

    suspend fun loadFrames(): List<UserPicture> {
        return presets.loadPresets()
            .filter { it.category == FRAMES_CATEGORY }
            .map {
                UriPicture(it.uri.toUri())
            }
    }

    fun loadUserPictures() = directory
        .listFiles()
        .filter { it.name.contains(".png") || it.name.contains(".jpg") }
        .sortedByDescending { it.lastModified() }
        .map {
            UriPicture(it.toUri())
        }

    suspend fun loadFillColors() = presets.loadFillColors()

    companion object {
        private const val PRESETS_URL = "http://postcardz.pythonanywhere.com"
        private const val FRAMES_CATEGORY = "Фоторамки"
    }
}
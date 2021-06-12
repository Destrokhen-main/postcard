package com.muroming.postcardeditor.data

import com.muroming.postcardeditor.data.dto.ApiPicture
import retrofit2.http.GET

interface PresetsApi {
    @GET("/frames")
    suspend fun loadPresets(): List<ApiPicture>

    @GET("/fill_colors")
    suspend fun loadFillColors(): List<String>
}
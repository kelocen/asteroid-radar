package dev.kelocen.asteroidradar.data.models

import com.squareup.moshi.Json

/**
 * A data class for NASA's image of the day.
 */
data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)
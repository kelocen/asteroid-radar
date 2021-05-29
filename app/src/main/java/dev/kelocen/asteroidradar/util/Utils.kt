package dev.kelocen.asteroidradar.util

import android.util.Base64
import dev.kelocen.asteroidradar.BuildConfig
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

/**
 * Retrieves the API key from the build config, decodes it from base 64, and returns it as
 * a string.
 */
fun getApiKey(): String {
    if (!BuildConfig.isKeyEncoded) {
        return BuildConfig.API_KEY
    }
    val encodedKey = BuildConfig.API_KEY
    val bytes = Base64.decode(encodedKey, Base64.URL_SAFE)
    return String(bytes, StandardCharsets.UTF_8)
}

/**
 * Returns a [String] that contains today's date.
 */
fun getDateToday(): String {
    val dateToday: String
    val calendar = Calendar.getInstance()
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    dateToday = dateFormat.format(currentTime)
    return dateToday
}

/**
 * Returns a [String] that contains the date seven days from today's date.
 */
fun getDateNextWeek(): String {
    val dateNextWeek: String
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    val futureTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    dateNextWeek = dateFormat.format(futureTime)
    return dateNextWeek
}

/**
 * Returns a [String] that contains yesterday's date.
 */
fun getDateYesterday(): String {
    val dateYesterday: String
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    val pastTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    dateYesterday = dateFormat.format(pastTime)
    return dateYesterday
}
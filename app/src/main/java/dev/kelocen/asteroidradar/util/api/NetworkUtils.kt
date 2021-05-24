package dev.kelocen.asteroidradar.util.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.os.Build
import dev.kelocen.asteroidradar.domain.Asteroid
import dev.kelocen.asteroidradar.network.NetworkAsteroid
import dev.kelocen.asteroidradar.util.Constants
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Returns an [ArrayList] of [Asteroid] objects.
 */
fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<NetworkAsteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")
    val asteroidList = ArrayList<NetworkAsteroid>()
    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)
        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter =
                asteroidJson.getJSONObject("estimated_diameter").getJSONObject("kilometers")
                    .getDouble("estimated_diameter_max")
            val closeApproachData =
                asteroidJson.getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth =
                closeApproachData.getJSONObject("miss_distance").getDouble("astronomical")
            val isPotentiallyHazardous =
                asteroidJson.getBoolean("is_potentially_hazardous_asteroid")
            val asteroid =
                NetworkAsteroid(id, codename, formattedDate, absoluteMagnitude, estimatedDiameter,
                        relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
            asteroidList.add(asteroid)
        }
    }
    return asteroidList
}

/**
 * Returns a formatted [ArrayList] of [String] objects that contain dates for the next 7 days.
 */
private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()
    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return formattedDateList
}

/**
 * Takes a [Context] argument and returns true if the device is connected to a network.
 */
fun isDeviceConnected(context: Context?): Boolean {
    var isConnected = false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val conMan = context?.getSystemService(ConnectivityManager::class.java)
        val capabilities = conMan?.getNetworkCapabilities(conMan.activeNetwork)
        if (capabilities?.hasCapability(
                    NET_CAPABILITY_VALIDATED) == true && capabilities.hasCapability(
                    NET_CAPABILITY_INTERNET)
        ) {
            isConnected = true
        }
    } else {
        val conMan = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = conMan.activeNetworkInfo
        if (network != null && network.isConnected) {
            isConnected = true
        }
    }
    return isConnected
}
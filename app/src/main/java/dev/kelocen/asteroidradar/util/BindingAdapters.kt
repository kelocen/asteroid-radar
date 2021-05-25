package dev.kelocen.asteroidradar.util

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.kelocen.asteroidradar.R
import dev.kelocen.asteroidradar.domain.Asteroid
import dev.kelocen.asteroidradar.domain.PictureOfDay
import dev.kelocen.asteroidradar.network.AsteroidApiStatus
import dev.kelocen.asteroidradar.network.PictureApiStatus
import dev.kelocen.asteroidradar.ui.asteroid.AsteroidAdapter

/**
 * Binding adapters for the asteroid screen.
 */

@BindingAdapter("pictureOfDay")
fun bindPictureOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    val context = imageView.context
    pictureOfDay?.let {
        val pictureUri = pictureOfDay.url.toUri().buildUpon()?.scheme("https")?.build()
        if (pictureOfDay.mediaType == "image") {
            Picasso.get().load(pictureUri).fit().centerCrop()
                .placeholder(R.drawable.placeholder_picture_of_day)
                .error(R.drawable.notify_image_broken)
                .into(imageView)
            imageView.contentDescription = String.format(
                    context.getString(R.string.picture_of_day_description), pictureOfDay.title)
        } else if (pictureOfDay.mediaType == "video") {
            imageView.setImageResource(R.drawable.notify_image_not_supported)
            imageView.contentDescription = context.getString(R.string.video_not_supported)
        }
    }
}

@BindingAdapter("pictureApiStatus")
fun bindPictureApiStatus(imageView: ImageView, status: PictureApiStatus?) {
    val context = imageView.context
    if (status == PictureApiStatus.ERROR) {
        imageView.setImageResource(R.drawable.notify_no_connection)
        imageView.contentDescription = context.getString(R.string.no_data_available)
    }
}

@BindingAdapter("asteroidList")
fun bindAsteroidRecycler(recyclerView: RecyclerView, asteroidList: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    if (asteroidList != null) {
        recyclerView.visibility = View.VISIBLE
        adapter.submitList(asteroidList)
    } else {
        recyclerView.visibility = View.INVISIBLE
    }
}

@BindingAdapter("listEmptyMessage")
fun bindEmptyMessage(textView: TextView, asteroids: LiveData<List<Asteroid>>?) {
    val context = textView.context
    if (asteroids?.value?.size == 0 || asteroids?.value == null) {
        textView.visibility = View.VISIBLE
        textView.text = context.getString(R.string.no_data_available)
    } else {
        textView.visibility = View.INVISIBLE
    }
}

@BindingAdapter("asteroidCodename")
fun bindAsteroidCodename(textView: TextView, codename: String) {
    textView.text = codename
}

@BindingAdapter("asteroidCloseApproachDate")
fun bindAsteroidCloseApproachDate(textView: TextView, approachDate: String) {
    textView.text = approachDate
}

@BindingAdapter("asteroidStatusImage")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = R.string.potentially_hazardous_asteroid_image.toString()
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = R.string.not_hazardous_asteroid_image.toString()
    }
}

@BindingAdapter("asteroidApiStatus")
fun bindAsteroidApiStatus(progressBar: ProgressBar, status: AsteroidApiStatus?) {
    when (status) {
        AsteroidApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        AsteroidApiStatus.ERROR -> {
            progressBar.visibility = View.INVISIBLE
        }
        AsteroidApiStatus.DONE -> {
            progressBar.visibility = View.INVISIBLE
        }
        else -> {
            progressBar.visibility = View.INVISIBLE
        }
    }
}

/**
 * Binding adapters for the detail screen.
 */
@BindingAdapter("detailStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        Picasso.get().load(R.drawable.asteroid_hazardous).fit().centerInside().into(imageView)
        imageView.contentDescription = R.string.potentially_hazardous_asteroid_image.toString()
    } else {
        Picasso.get().load(R.drawable.asteroid_safe).fit().centerInside().into(imageView)
        imageView.contentDescription = R.string.not_hazardous_asteroid_image.toString()
    }
}

@BindingAdapter("detailAstronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("detailKmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("detailVelocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
package dev.kelocen.asteroidradar.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.kelocen.asteroidradar.R

/**
 * Binding adapters for the asteroid fragment.
 */
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
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

/**
 * Binding adapters for the detail fragment.
 */
@BindingAdapter("detailStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
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
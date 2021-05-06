package dev.kelocen.asteroidradar.ui.asteroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.kelocen.asteroidradar.R
import dev.kelocen.asteroidradar.data.models.Asteroid

/**
 * A subclass of [RecyclerView.Adapter] for [Asteroid] objects.
 */
class AsteroidAdapter : RecyclerView.Adapter<AsteroidAdapter.AsteroidHolder>() {

    /**
     * A list of [Asteroid] objects for the [AsteroidAdapter].
     */
    var asteroids = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = asteroids.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidHolder {
        return AsteroidHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidHolder, position: Int) {
        val asteroidItem = asteroids[position]
        holder.bind(asteroidItem)
    }

    /**
     * A ViewHolder class for [AsteroidAdapter].
     *
     */
    class AsteroidHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val codename: TextView = itemView.findViewById(R.id.text_asteroid_code_name)
        private val approachDate: TextView = itemView.findViewById(R.id.text_asteroid_approach_date)
        private val potentiallyDangerous: ImageView = itemView.findViewById(
                R.id.image_potentially_dangerous)

        /**
         * Binds [View] properties to corresponding [Asteroid] properties.
         */
        fun bind(item: Asteroid) {
            codename.text = item.codename
            approachDate.text = item.closeApproachDate
            potentiallyDangerous.setImageResource(when (item.isPotentiallyHazardous) {
                true -> R.drawable.ic_status_potentially_hazardous
                false -> R.drawable.ic_status_normal
            })
        }

        /**
         * A companion object for [AsteroidHolder]
         */
        companion object {
            fun from(parent: ViewGroup): AsteroidHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_asteroid, parent, false)
                return AsteroidHolder(view)
            }
        }
    }
}
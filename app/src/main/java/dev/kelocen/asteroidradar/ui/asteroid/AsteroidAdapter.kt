package dev.kelocen.asteroidradar.ui.asteroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kelocen.asteroidradar.data.models.Asteroid
import dev.kelocen.asteroidradar.databinding.ListItemAsteroidBinding

/**
 * A subclass of [RecyclerView.Adapter] for [Asteroid] objects.
 */
class AsteroidAdapter(private val clickListener: AsteroidListener) :
    RecyclerView.Adapter<AsteroidAdapter.AsteroidHolder>() {

    /**
     * A list of [Asteroid] objects for the [AsteroidAdapter].
     */
    var asteroids = listOf<Asteroid?>()
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
        holder.bind(clickListener, asteroidItem)
    }

    /**
     * A ViewHolder class for [AsteroidAdapter].
     */
    class AsteroidHolder(private val binding: ListItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Initializes list item bindings with [Asteroid] data.
         */
        fun bind(clickListener: AsteroidListener, asteroid: Asteroid?) {
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        /**
         * A companion object for [AsteroidHolder].
         */
        companion object {
            fun from(parent: ViewGroup): AsteroidHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
                return AsteroidHolder(binding)
            }
        }
    }

    /**
     * A click listener class for [AsteroidAdapter].
     */
    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}
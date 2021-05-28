package dev.kelocen.asteroidradar.ui.asteroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kelocen.asteroidradar.databinding.ListItemAsteroidBinding
import dev.kelocen.asteroidradar.domain.Asteroid

/**
 * A subclass of [RecyclerView.Adapter] for [Asteroid] objects.
 */
class AsteroidAdapter(private val clickListener: AsteroidListener) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(AsteroidDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: AsteroidViewHolder, position: Int) {
        val asteroidItem = getItem(position)
        viewHolder.bind(clickListener, asteroidItem)
    }

    /**
     * A ViewHolder class for [AsteroidAdapter].
     */
    class AsteroidViewHolder(private val binding: ListItemAsteroidBinding) :
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
         * A companion object for [AsteroidViewHolder].
         */
        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }

    /**
     * A companion object that implements [DiffUtil.ItemCallback] to optimize date refreshing.
     */
    companion object AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * A click listener class for [AsteroidAdapter].
     */
    class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}
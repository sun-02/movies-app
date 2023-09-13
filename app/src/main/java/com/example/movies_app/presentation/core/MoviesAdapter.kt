package com.example.movies_app.presentation.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.movies_app.databinding.ItemMoviesBinding
import com.example.movies_app.domain.movies.Movie

class MoviesAdapter(
    private val onEvent: (MoviesListItemEvent) -> Unit
) : ListAdapter<Movie, MoviesViewHolder>(DiffCallback) {

    private lateinit var binding: ItemMoviesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        binding = ItemMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding, onEvent)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == MoviesViewHolder.FAVOURITE_IS_CHANGED) {
                val movie = getItem(position)
                holder.bindFavoriteState(movie)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.id.value == newItem.id.value

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: Movie, newItem: Movie): Any? {
            return if (!oldItem.isFavourite == newItem.isFavourite) {
                MoviesViewHolder.FAVOURITE_IS_CHANGED
            } else null
        }
    }
}

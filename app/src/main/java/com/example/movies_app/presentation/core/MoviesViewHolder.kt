package com.example.movies_app.presentation.core

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movies_app.databinding.ItemMoviesBinding
import com.example.movies_app.domain.movies.Movie

class MoviesViewHolder(
    private val binding: ItemMoviesBinding,
    private val onEvent: (MoviesListItemEvent) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie) {
        with (binding) {
            root.setOnClickListener {
                onEvent(MoviesListItemEvent.OnItemClick(movie.id))
            }
            poster.load(movie.previewUrl)
            title.text = movie.title
            imdbRating.text = movie.imdbRating.toString()
            isFavourite.isChecked = movie.isFavourite
            isFavourite.setOnClickListener {
                onEvent(MoviesListItemEvent.OnLikeClick(movie))
            }
        }
    }

    fun bindFavoriteState(movie: Movie) {
        with (binding.isFavourite) {
            isChecked = movie.isFavourite
            setOnClickListener {
                onEvent(MoviesListItemEvent.OnLikeClick(movie))
            }
        }
    }

    companion object {
        const val FAVOURITE_IS_CHANGED = "FavouriteIsChanged"
    }
}
package com.example.movies_app.presentation.movie_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.movies_app.databinding.ItemActorsBinding
import com.example.movies_app.domain.movie_detail.MovieActor

class MovieActorsAdapter(
    private val onEvent: (ActorName) -> Unit
) : ListAdapter<MovieActor, MovieActorsViewHolder>(DiffCallback) {

    private lateinit var binding: ItemActorsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieActorsViewHolder {
        binding = ItemActorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieActorsViewHolder(binding, onEvent)
    }


    override fun onBindViewHolder(holder: MovieActorsViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MovieActor>() {
        override fun areItemsTheSame(oldItem: MovieActor, newItem: MovieActor): Boolean =
            oldItem.actorName == newItem.actorName

        override fun areContentsTheSame(oldItem: MovieActor, newItem: MovieActor): Boolean =
            oldItem == newItem
    }
}
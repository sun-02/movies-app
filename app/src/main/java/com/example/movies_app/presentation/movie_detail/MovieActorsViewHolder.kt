package com.example.movies_app.presentation.movie_detail

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movies_app.databinding.ItemActorsBinding
import com.example.movies_app.domain.movie_detail.MovieActor

class MovieActorsViewHolder(
    private val binding: ItemActorsBinding,
    private val onEvent: (ActorName) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(actor: MovieActor) {
        with(binding) {
            root.setOnClickListener {
                onEvent(ActorName(actor.actorName))
            }
            actorPhoto.load(actor.imageUrl)
            actorFullName.text = actor.actorName
            role.text = actor.characterName
        }
    }
}
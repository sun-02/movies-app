package com.example.movies_app.data.movies.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "imdb_rating") val imdbRating: Double,
    @ColumnInfo(name = "imdb_votes") val imdbVotes: Int,
    @ColumnInfo(name = "preview_url") val previewUrl: String,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean
)

package com.dicoding.moviecatalog.data.model

data class FilmModel(
    val title: String?,
    val rating: Float,
    val releaseDate: String,
    val genres: List<String>,
    val synopsis: String,
    val type: String,
    val poster: String,
    val id: Int
)

package com.dicoding.moviecatalog.data.model.jsonmodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieModelJson(
    @SerialName("original_title")
    val title: String = "",
    @SerialName("vote_average")
    val rating: Float = 0f,
    @SerialName("release_date")
    val releaseDate: String = "",
    @SerialName("genres")
    val genres: List<Genre> = emptyList(),
    @SerialName("overview")
    val synopsis: String ="",
    @SerialName("media_type")
    val type: String = "",
    @SerialName("poster_path")
    val poster: String = "",
    @SerialName("id")
    val id: Int = 0
)
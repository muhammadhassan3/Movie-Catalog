package com.dicoding.moviecatalog.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilmModel(
    val title: String,
    val rating: Float,
    val releaseDate: String,
    val genres: List<String>,
    val synopsis: String,
    val type: FilmType,
    val poster: String
): Parcelable

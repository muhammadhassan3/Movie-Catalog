package com.dicoding.moviecatalog.utils

import kotlinx.serialization.json.Json

object Constant {
    const val ImageBaseUrl = "https:/image.tmdb.org/t/p/w500"
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    const val NETWORK_PAGE_SIZE = 20
    const val TMDB_STARTING_PAGE = 1
}
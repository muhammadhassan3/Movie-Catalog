package com.dicoding.moviecatalog.utils

import com.dicoding.moviecatalog.data.model.jsonmodel.TmdbModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

// test purpose
object JsonHelper {
    private val jsonBuilder = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun loadMovieListFromJson(jsonString: String): TmdbModel<MovieModelJson> = jsonBuilder.decodeFromString(jsonString)

    fun loadSeriesListFromJson(jsonString: String): TmdbModel<SeriesModelJson> = jsonBuilder.decodeFromString(jsonString)

    fun loadMovieModelFromJson(jsonString: String): MovieModelJson = jsonBuilder.decodeFromString(jsonString)

    fun loadSeriesModelFromJson(jsonString: String): SeriesModelJson = jsonBuilder.decodeFromString(jsonString)

    fun loadTrendingModelFromJson(jsonString: String): TmdbModel<RandomModelJson> = jsonBuilder.decodeFromString(jsonString)

    fun parseStringFromJsonResource(fileName: String): String{
        val inputStream = javaClass.classLoader!!.getResourceAsStream("json/$fileName")
        val source = inputStream.source().buffer()
        return source.readString(StandardCharsets.UTF_8)
    }

}
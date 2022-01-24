package com.dicoding.moviecatalog.utils

import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.TmdbModel

val movieDummy = FilmModel(
    title = "Sing 2",
    rating = 8.3f,
    releaseDate = "2021-12-01",
    genres = listOf("Animasi", "Komedi", "Keluarga", "Musik"),
    synopsis = "",
    type = "",
    poster = "/aWeKITRFbbwY8txG5uCj4rMCfSP.jpg",
    id = 438695
)

val seriesDummy = FilmModel(
    title = "Euphoria",
    rating = 8.4f,
    releaseDate = "2019-06-16",
    genres = listOf("Drama"),
    synopsis = "",
    type = "",
    poster = "/jtnfNzqZwN4E32FGGxx1YZaBWWf.jpg",
    id = 85552
)

const val moviesCount = 20
const val seriesCount = 20
const val randomCount = 10


fun provideMovieListFromJson(): TmdbModel<MovieModelJson> {
    return JsonHelper.loadMovieListFromJson(JsonHelper.parseStringFromJsonResource("popularMovies.json"))
}

fun providePopularListFromJson(): TmdbModel<RandomModelJson> {
    return JsonHelper.loadTrendingModelFromJson(JsonHelper.parseStringFromJsonResource("trendingFilm.json"))
}

fun provideSeriesListFromJson(): TmdbModel<SeriesModelJson> {
    return JsonHelper.loadSeriesListFromJson(JsonHelper.parseStringFromJsonResource("popularSeries.json"))
}

fun provideMovieDummyData(): MovieModelJson {
    return JsonHelper.loadMovieModelFromJson(JsonHelper.parseStringFromJsonResource("movieDetail.json"))
}

fun provideSeriesDummyData(): SeriesModelJson{
    return JsonHelper.loadSeriesModelFromJson(JsonHelper.parseStringFromJsonResource("seriesDetail.json"))
}

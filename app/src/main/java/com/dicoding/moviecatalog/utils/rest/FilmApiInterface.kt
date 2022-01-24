package com.dicoding.moviecatalog.utils.rest

import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.TmdbModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FilmApiInterface {

    @GET("trending/all/day?language=id-ID")
    fun getTrending(): Call<TmdbModel<RandomModelJson>>

    @GET("tv/popular?language=id-ID")
    fun getSeries(): Call<TmdbModel<SeriesModelJson>>

    @GET("movie/popular?language=id-ID")
    fun getMovies(): Call<TmdbModel<MovieModelJson>>

    @GET("movie/{id}?language=id-ID")
    fun getMoviesDetail(@Path("id") id: Int): Call<MovieModelJson>

    @GET("tv/{id}?language=id-ID")
    fun getSeriesDetail(@Path("id") id: Int): Call<SeriesModelJson>
}
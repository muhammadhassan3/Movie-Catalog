package com.dicoding.moviecatalog.utils.rest

import com.dicoding.moviecatalog.data.model.jsonmodel.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApiInterface {

    @GET("trending/all/week?language=id-ID")
    suspend fun getTrending(@Query("page")page: Int): Response<TmdbModel<RandomModelJson>>

    @GET("tv/popular?language=id-ID")
    suspend fun getSeries(@Query("page")page: Int): Response<TmdbModel<SeriesModelJson>>

    @GET("movie/popular?language=id-ID")
    suspend fun getMovies(@Query("page")page: Int): Response<TmdbModel<MovieModelJson>>

    @GET("movie/{id}?language=id-ID")
    fun getMoviesDetail(@Path("id") id: Int): Call<MovieModelJson>

    @GET("tv/{id}?language=id-ID")
    fun getSeriesDetail(@Path("id") id: Int): Call<SeriesModelJson>

    @GET("search/multi?language=id-ID&include_adult=false")
    suspend fun getSearchResult(@Query("page")page: Int, @Query("query") query: String): Response<TmdbModel<RandomModelJson>>
}
package com.dicoding.moviecatalog.data.datasource

import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.utils.*
import com.dicoding.moviecatalog.utils.rest.FilmApiInterface

open class RemoteDataSource private constructor(private val api: FilmApiInterface) {
    suspend fun loadTrending(page: Int): ApiResponse<List<FilmModel>> {
        val response = api.getTrending(page)
        return if (response.isSuccessful) {
            val data = response.body()?.results
            if (data != null) {
                ApiResponse.success(data.map(RandomModelJson::asDomain), null)
            } else ApiResponse.noData()
        }else ApiResponse.error(provideRequestErrorMessage(response.code(), response.message()))
    }

    suspend fun loadMovies(page: Int): ApiResponse<List<FilmModel>> {
        val response = api.getMovies(page)
        return if (response.isSuccessful) {
            val data = response.body()?.results
            if (data != null) {
                ApiResponse.success(data.map(MovieModelJson::asDomain), null)
            } else ApiResponse.noData()
        } else ApiResponse.error(provideRequestErrorMessage(response.code(), response.message()))
    }

    suspend fun loadSeries(page: Int): ApiResponse<List<FilmModel>> {
        val response = api.getSeries(page)
        return if (response.isSuccessful) {
            val data = response.body()?.results
            if (data != null) {
                ApiResponse.success(data.map(SeriesModelJson::asDomain), null)
            } else ApiResponse.noData()
        } else ApiResponse.error(provideRequestErrorMessage(response.code(), response.message()))
    }

    suspend fun getSearchResult(page: Int, query: String): ApiResponse<List<FilmModel>>{
        val response = api.getSearchResult(page, query)
        return if(response.isSuccessful){
            val data = response.body()?.results
            if(data != null) {
                ApiResponse.success(data.map(RandomModelJson::asDomain), null)
            }else ApiResponse.noData()
        }else ApiResponse.error(provideRequestErrorMessage(response.code(), response.message()))
    }

    fun loadMovieDetail(id: Int, callback: LoadMovieDetail) {
        increase()
        val request = api.getMoviesDetail(id)
        callback.onRequestCalled()
        request.enqueue(object : CallbackWithRetry<MovieModelJson>() {
            override fun onResponseSuccess(data: MovieModelJson?) {
                if (data != null) {
                    callback.onResponseSuccess(data.asDomain())
                } else callback.onNoDataReceived()
                decrease()
            }

            override fun onResponseFailed(errorCode: Int, errorMessage: String) {
                callback.onErrorResponse(errorCode, errorMessage)
                decrease()
            }

            override fun onConnectionError(message: String) {
                callback.onConnectionError(message)
                decrease()
            }

        })
    }

    fun loadSeriesDetail(id: Int, callback: LoadSeriesDetail) {
        increase()
        val request = api.getSeriesDetail(id)
        callback.onRequestCalled()
        request.enqueue(object : CallbackWithRetry<SeriesModelJson>() {
            override fun onResponseSuccess(data: SeriesModelJson?) {
                if (data != null) {
                    callback.onResponseSuccess(data.asDomain())
                } else callback.onNoDataReceived()
                decrease()
            }

            override fun onResponseFailed(errorCode: Int, errorMessage: String) {
                callback.onErrorResponse(errorCode, errorMessage)
                decrease()
            }

            override fun onConnectionError(message: String) {
                callback.onConnectionError(message)
                decrease()
            }

        })
    }

    private fun increase() {
        EspressoIdlingResources.increment()
    }

    private fun decrease() {
        EspressoIdlingResources.decrement()
    }

    interface LoadFromApiCallback {
        fun onRequestCalled()
        fun onNoDataReceived()
        fun onErrorResponse(errorCode: Int, errorMessage: String)
        fun onConnectionError(errorMessage: String)
    }

    interface LoadMoviesList : LoadFromApiCallback {
        fun onResponseSuccess(list: List<FilmModel>)
    }

    interface LoadSeriesList : LoadFromApiCallback {
        fun onResponseSuccess(list: List<FilmModel>)
    }

    interface LoadTrendingList : LoadFromApiCallback {
        fun onResponseSuccess(list: List<FilmModel>)
    }

    interface LoadMovieDetail : LoadFromApiCallback {
        fun onResponseSuccess(data: FilmModel)
    }

    interface LoadSeriesDetail : LoadFromApiCallback {
        fun onResponseSuccess(data: FilmModel)
    }

    companion object {
        @Volatile
        private var INSTANCE: RemoteDataSource? = null

        fun getInstance(api: FilmApiInterface): RemoteDataSource =
            INSTANCE ?: synchronized(this) {
                val instance = RemoteDataSource(api)
                INSTANCE = instance
                instance
            }
    }
}
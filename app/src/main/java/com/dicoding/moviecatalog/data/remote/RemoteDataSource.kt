package com.dicoding.moviecatalog.data.remote

import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.TmdbModel
import com.dicoding.moviecatalog.utils.CallbackWithRetry
import com.dicoding.moviecatalog.utils.EspressoIdlingResources
import com.dicoding.moviecatalog.utils.asDomain
import com.dicoding.moviecatalog.utils.rest.FilmApiInterface

open class RemoteDataSource(private val api: FilmApiInterface) {
    fun loadRandom(size: Int, callback: LoadTrendingList) {
        increase()
        val request = api.getTrending()
        callback.onRequestCalled()
        request.enqueue(object : CallbackWithRetry<TmdbModel<RandomModelJson>>() {
            override fun onResponseSuccess(data: TmdbModel<RandomModelJson>?) {
                if (data?.results != null) {
                    callback.onResponseSuccess(
                        data.results.map(RandomModelJson::asDomain).chunked(size)[0]
                    )
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

    fun loadMovies(callback: LoadMoviesList) {
        increase()
        val request = api.getMovies()
        callback.onRequestCalled()
        request.enqueue(object : CallbackWithRetry<TmdbModel<MovieModelJson>>() {
            override fun onResponseSuccess(data: TmdbModel<MovieModelJson>?) {
                if (data?.results != null) {
                    callback.onResponseSuccess(data.results.map(MovieModelJson::asDomain))
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

    fun loadSeries(callback: LoadSeriesList) {
        increase()
        val request = api.getSeries()
        callback.onRequestCalled()
        request.enqueue(object : CallbackWithRetry<TmdbModel<SeriesModelJson>>() {
            override fun onResponseSuccess(data: TmdbModel<SeriesModelJson>?) {
                if (data?.results != null) {
                    callback.onResponseSuccess(data.results.map(SeriesModelJson::asDomain))
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

    fun loadMoviesDetail(id: Int, callback: LoadMovieDetail) {
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
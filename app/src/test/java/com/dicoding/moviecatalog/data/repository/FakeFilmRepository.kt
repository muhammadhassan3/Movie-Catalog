package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.remote.RemoteDataSource
import com.dicoding.moviecatalog.utils.ApiResponse
import com.dicoding.moviecatalog.utils.provideRequestErrorMessage

class FakeFilmRepository(private val dataSource: RemoteDataSource): FilmRepository {
    override fun loadRandom(
        size: Int
    ): LiveData<ApiResponse<List<FilmModel>>> {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        dataSource.loadRandom(size, object : RemoteDataSource.LoadTrendingList {
            override fun onResponseSuccess(list: List<FilmModel>) {
                result.postValue(ApiResponse.success(list, null))
            }

            override fun onRequestCalled() {
                result.postValue(ApiResponse.loading())
            }

            override fun onNoDataReceived() {
                result.postValue(ApiResponse.noData())
            }

            override fun onErrorResponse(errorCode: Int, errorMessage: String) {
                result.postValue(
                    ApiResponse.error(
                        provideRequestErrorMessage(
                            errorCode,
                            errorMessage
                        )
                    )
                )
            }

            override fun onConnectionError(errorMessage: String) {
                result.postValue(ApiResponse.error(errorMessage))
            }

        })

        return result
    }

    override fun loadMovies(): LiveData<ApiResponse<List<FilmModel>>> {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        dataSource.loadMovies(object : RemoteDataSource.LoadMoviesList {
            override fun onResponseSuccess(list: List<FilmModel>) {
                result.postValue(ApiResponse.success(list, null))
            }

            override fun onRequestCalled() {
                result.postValue(ApiResponse.loading())
            }

            override fun onNoDataReceived() {
                result.postValue(ApiResponse.noData())
            }

            override fun onErrorResponse(errorCode: Int, errorMessage: String) {
                result.postValue(ApiResponse.error(provideRequestErrorMessage(errorCode, errorMessage)))
            }

            override fun onConnectionError(errorMessage: String) {
                result.postValue(ApiResponse.error(errorMessage))
            }

        })
        return result
    }

    override fun loadSeries(): LiveData<ApiResponse<List<FilmModel>>> {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        dataSource.loadSeries(object : RemoteDataSource.LoadSeriesList{
            override fun onResponseSuccess(list: List<FilmModel>) {
                result.postValue(ApiResponse.success(list, null))
            }

            override fun onRequestCalled() {
                result.postValue(ApiResponse.loading())
            }

            override fun onNoDataReceived() {
                result.postValue(ApiResponse.noData())
            }

            override fun onErrorResponse(errorCode: Int, errorMessage: String) {
                result.postValue(ApiResponse.error(provideRequestErrorMessage(errorCode, errorMessage)))
            }

            override fun onConnectionError(errorMessage: String) {
                result.postValue(ApiResponse.error(errorMessage))
            }

        })

        return result
    }

    override fun loadMoviesDetail(
        id: Int
    ): LiveData<ApiResponse<FilmModel>> {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        dataSource.loadMoviesDetail(id, object: RemoteDataSource.LoadMovieDetail{
            override fun onResponseSuccess(data: FilmModel) {
                result.postValue(ApiResponse.success(data, null))
            }

            override fun onRequestCalled() {
                result.postValue(ApiResponse.loading())
            }

            override fun onNoDataReceived() {
                result.postValue(ApiResponse.noData())
            }

            override fun onErrorResponse(errorCode: Int, errorMessage: String) {
                result.postValue(ApiResponse.error(provideRequestErrorMessage(errorCode, errorMessage)))
            }

            override fun onConnectionError(errorMessage: String) {
                result.postValue(ApiResponse.error(errorMessage))
            }

        })
        return result
    }

    override fun loadSeriesDetail(
        id: Int
    ): LiveData<ApiResponse<FilmModel>> {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        dataSource.loadSeriesDetail(id, object: RemoteDataSource.LoadSeriesDetail{
            override fun onResponseSuccess(data: FilmModel) {
                result.postValue(ApiResponse.success(data, null))
            }

            override fun onRequestCalled() {
                result.postValue(ApiResponse.loading())
            }

            override fun onNoDataReceived() {
                result.postValue(ApiResponse.noData())
            }

            override fun onErrorResponse(errorCode: Int, errorMessage: String) {
                result.postValue(ApiResponse.error(provideRequestErrorMessage(errorCode, errorMessage)))
            }

            override fun onConnectionError(errorMessage: String) {
                result.postValue(ApiResponse.error(errorMessage))
            }

        })
        return result
    }
}
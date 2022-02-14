package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.datasource.RemoteDataSource
import com.dicoding.moviecatalog.utils.ApiResponse
import com.dicoding.moviecatalog.data.pagingsource.MoviesPagingSource
import com.dicoding.moviecatalog.data.pagingsource.SearchPagingSource
import com.dicoding.moviecatalog.data.pagingsource.SeriesPagingSource
import com.dicoding.moviecatalog.data.pagingsource.TrendingPagingSource
import com.dicoding.moviecatalog.utils.provideRequestErrorMessage
import kotlinx.coroutines.flow.Flow

class FilmRepositoryImpl(private val remote: RemoteDataSource,
) : FilmRepository {
    override fun loadTrending(): Flow<PagingData<FilmModel>> {
        return Pager(config = PagingConfig(20)){
            TrendingPagingSource(remote)
        }.flow
    }

    override fun loadMovies(): Flow<PagingData<FilmModel>> {
        return Pager(config = PagingConfig(20)){
            MoviesPagingSource(remote)
        }.flow
    }

    override fun loadSeries(): Flow<PagingData<FilmModel>> {
        return Pager(config = PagingConfig(20)){
            SeriesPagingSource(remote)
        }.flow
    }

    override fun getSearchResult(query: String): LiveData<PagingData<FilmModel>> {
        return Pager(config = PagingConfig(20)){
            SearchPagingSource(remote, query)
        }.liveData
    }

    override fun loadMoviesDetail(
        id: Int
    ): LiveData<ApiResponse<FilmModel>> {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        remote.loadMovieDetail(id, object: RemoteDataSource.LoadMovieDetail{
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
        remote.loadSeriesDetail(id, object: RemoteDataSource.LoadSeriesDetail{
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

    companion object {
        @Volatile
        private var INSTANCE: FilmRepositoryImpl? = null

        fun getInstance(remote: RemoteDataSource): FilmRepositoryImpl =
            INSTANCE ?: synchronized(this) {
                val instance = FilmRepositoryImpl(remote)
                INSTANCE = instance
                instance
            }
    }
}
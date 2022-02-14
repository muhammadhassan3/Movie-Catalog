package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.moviecatalog.data.datasource.RemoteDataSource
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.utils.ApiResponse
import com.dicoding.moviecatalog.data.pagingsource.MoviesPagingSource
import com.dicoding.moviecatalog.data.pagingsource.SearchPagingSource
import com.dicoding.moviecatalog.data.pagingsource.SeriesPagingSource
import com.dicoding.moviecatalog.data.pagingsource.TrendingPagingSource
import com.dicoding.moviecatalog.utils.provideRequestErrorMessage
import kotlinx.coroutines.flow.Flow

class FakeFilmRepository(private val dataSource: RemoteDataSource) : FilmRepository {
    override fun loadTrending(): Flow<PagingData<FilmModel>> = Pager(
        config = PagingConfig(1)
    ) {
        TrendingPagingSource(dataSource)
    }.flow

    override fun loadMovies(): Flow<PagingData<FilmModel>> = Pager(
        config = PagingConfig(1)
    ) {
        MoviesPagingSource(dataSource)
    }.flow

    override fun loadSeries(): Flow<PagingData<FilmModel>> = Pager(
        config = PagingConfig(1)
    ) {
        SeriesPagingSource(dataSource)
    }.flow

    override fun getSearchResult(query: String): LiveData<PagingData<FilmModel>> = Pager(
        config = PagingConfig(1)
    ) {
        SearchPagingSource(dataSource, query)
    }.liveData


    override fun loadMoviesDetail(
        id: Int
    ): LiveData<ApiResponse<FilmModel>> {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        dataSource.loadMovieDetail(id, object : RemoteDataSource.LoadMovieDetail {
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

    override fun loadSeriesDetail(
        id: Int
    ): LiveData<ApiResponse<FilmModel>> {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        dataSource.loadSeriesDetail(id, object : RemoteDataSource.LoadSeriesDetail {
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
}
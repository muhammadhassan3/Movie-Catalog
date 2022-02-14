package com.dicoding.moviecatalog.data.interactor

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.repository.FavoriteRepository
import com.dicoding.moviecatalog.data.usecase.MovieFavoriteUseCase
import com.dicoding.moviecatalog.utils.QueryCollection.MOVIE

class MovieFavoriteInteractor(private val repository: FavoriteRepository): MovieFavoriteUseCase {
    override fun getMoviesList(): LiveData<PagingData<FavoriteEntity>> = repository.getFavoriteData(MOVIE)

    override fun getMoviesSize(): LiveData<Int> = repository.getFavoriteDataCount(MOVIE)

}
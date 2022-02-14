package com.dicoding.moviecatalog.data.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity

interface MovieFavoriteUseCase {
    fun getMoviesList(): LiveData<PagingData<FavoriteEntity>>
    fun getMoviesSize(): LiveData<Int>
}
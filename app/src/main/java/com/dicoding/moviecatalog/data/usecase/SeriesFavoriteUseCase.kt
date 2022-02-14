package com.dicoding.moviecatalog.data.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity

interface SeriesFavoriteUseCase {
    fun getSeriesList(): LiveData<PagingData<FavoriteEntity>>
    fun getSeriesSize(): LiveData<Int>
}
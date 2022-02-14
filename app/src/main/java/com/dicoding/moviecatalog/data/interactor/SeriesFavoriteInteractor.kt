package com.dicoding.moviecatalog.data.interactor

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.repository.FavoriteRepository
import com.dicoding.moviecatalog.data.usecase.SeriesFavoriteUseCase
import com.dicoding.moviecatalog.utils.QueryCollection.TV_SHOW

class SeriesFavoriteInteractor(private val repository: FavoriteRepository): SeriesFavoriteUseCase {
    override fun getSeriesList(): LiveData<PagingData<FavoriteEntity>> = repository.getFavoriteData(TV_SHOW)

    override fun getSeriesSize(): LiveData<Int> = repository.getFavoriteDataCount(TV_SHOW)
}
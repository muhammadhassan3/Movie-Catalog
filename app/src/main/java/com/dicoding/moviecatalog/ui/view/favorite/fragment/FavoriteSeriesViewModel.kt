package com.dicoding.moviecatalog.ui.view.favorite.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.usecase.SeriesFavoriteUseCase

class FavoriteSeriesViewModel(private val useCase: SeriesFavoriteUseCase): ViewModel() {
    fun getSize() = useCase.getSeriesSize()
    fun getData() = useCase.getSeriesList()
}
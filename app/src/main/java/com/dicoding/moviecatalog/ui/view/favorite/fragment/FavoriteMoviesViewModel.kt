package com.dicoding.moviecatalog.ui.view.favorite.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.usecase.MovieFavoriteUseCase

class FavoriteMoviesViewModel(private val useCase: MovieFavoriteUseCase): ViewModel() {
    fun getData(): LiveData<PagingData<FavoriteEntity>> = useCase.getMoviesList()
    fun getSize(): LiveData<Int> = useCase.getMoviesSize()
}
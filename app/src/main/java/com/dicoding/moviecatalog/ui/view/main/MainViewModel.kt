package com.dicoding.moviecatalog.ui.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.usecase.FilmListUseCase
import com.dicoding.moviecatalog.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val useCase: FilmListUseCase): ViewModel() {

    fun getMovies()= useCase.getPopularMovies()

    fun getSeries()= useCase.getPopularSeries()

    fun getTrending() = useCase.getPopularFilm()
}
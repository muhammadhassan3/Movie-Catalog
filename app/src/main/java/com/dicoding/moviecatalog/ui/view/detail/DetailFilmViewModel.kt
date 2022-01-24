package com.dicoding.moviecatalog.ui.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.usecase.FilmDetailUseCase
import com.dicoding.moviecatalog.utils.ApiResponse

class DetailFilmViewModel(private val useCase: FilmDetailUseCase) : ViewModel() {
    lateinit var data: LiveData<ApiResponse<FilmModel>>

    fun setData(id: Int, type: String) {
        data = if (type == "tv") {
            useCase.getDetailSeries(id)
        } else {
            useCase.getDetailMovie(id)
        }
    }
}
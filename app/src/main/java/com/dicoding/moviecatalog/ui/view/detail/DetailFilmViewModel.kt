package com.dicoding.moviecatalog.ui.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.moviecatalog.data.model.FilmModel

class DetailFilmViewModel: ViewModel() {
    private val _data = MutableLiveData<FilmModel?>()
    val data: LiveData<FilmModel?> get() = _data

    fun setData(filmModel: FilmModel?) {
        _data.value = filmModel
    }
}
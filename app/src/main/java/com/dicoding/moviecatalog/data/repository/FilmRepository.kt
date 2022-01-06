package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.moviecatalog.data.model.DummyData
import com.dicoding.moviecatalog.data.model.FilmModel

class FilmRepository {
    private val dummyData = DummyData()
    private val _random = MutableLiveData<List<FilmModel>>()
    private val _movies = MutableLiveData<List<FilmModel>>()
    private val _series = MutableLiveData<List<FilmModel>>()
    val movies: LiveData<List<FilmModel>> get() = _movies
    val series: LiveData<List<FilmModel>> get() = _series
    val random: LiveData<List<FilmModel>> get() = _random

    init {
        setSeries()
        setMovies()
    }

    fun setMovies() {
        _movies.value = dummyData.setMovies()
    }

    fun setSeries() {
        _series.value = dummyData.setSeries()
    }

    fun setRandom(size: Int) {
        _random.value = dummyData.setRandom(size)
    }
}
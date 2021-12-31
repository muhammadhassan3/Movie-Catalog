package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.moviecatalog.data.model.DummyData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.FilmType

class FilmRepository {
    private val data = DummyData().getData()
    private val _random = MutableLiveData<List<FilmModel>>()
    private val _movies = MutableLiveData<List<FilmModel>>()
    private val _series = MutableLiveData<List<FilmModel>>()
    val movies: LiveData<List<FilmModel>> get() = _movies
    val series: LiveData<List<FilmModel>> get() = _series
    val random: LiveData<List<FilmModel>> get() = _random

    fun setMovies(){
        val result = arrayListOf<FilmModel>()
        for(item in data)
            if(item.type == FilmType.MOVIE) result.add(item)
        _movies.value = result
    }

    fun setSeries(){
        val result = arrayListOf<FilmModel>()
        for(item in data)
            if(item.type == FilmType.SERIES) result.add(item)
        _series.value = result
    }

    fun setRandom(size: Int){
        val result = data.shuffled()
        val chunk = result.chunked(size)
        _random.value = chunk[0]
    }
}
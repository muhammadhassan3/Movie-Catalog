package com.dicoding.moviecatalog.data.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.moviecatalog.data.model.FilmModel

interface SearchUseCase {
    fun findDataByQuery(query: String): LiveData<PagingData<FilmModel>>
}
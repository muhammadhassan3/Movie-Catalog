package com.dicoding.moviecatalog.data.interactor

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.repository.FilmRepository
import com.dicoding.moviecatalog.data.usecase.SearchUseCase

class SearchInteractor(private val repository: FilmRepository): SearchUseCase {
    override fun findDataByQuery(query: String): LiveData<PagingData<FilmModel>> = repository.getSearchResult(query)
}
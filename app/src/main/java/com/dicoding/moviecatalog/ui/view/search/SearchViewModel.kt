package com.dicoding.moviecatalog.ui.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dicoding.moviecatalog.data.usecase.SearchUseCase

class SearchViewModel(private val useCase: SearchUseCase): ViewModel() {
    private val searchQuery = MutableLiveData<String>()

    fun setQuery(query: String){
        searchQuery.value = query
    }

    fun getSearchResult() = searchQuery.switchMap {
        useCase.findDataByQuery(it).cachedIn(viewModelScope)
    }

}
package com.dicoding.moviecatalog.ui.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import androidx.paging.PagingData
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.usecase.SearchUseCase
import com.dicoding.moviecatalog.utils.LiveDataTestUtil.value
import com.dicoding.moviecatalog.utils.asDomain
import com.dicoding.moviecatalog.utils.provideSearchResultData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    @get:Rule
    val tasKExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val useCase = mock<SearchUseCase>()
    private lateinit var viewModel: SearchViewModel

    private val searchQuery = "spiderman"
    private val searchResultData = provideSearchResultData().results.map(RandomModelJson::asDomain)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = SearchViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getSearchResult() {
        whenever(useCase.findDataByQuery(searchQuery)).thenReturn(liveData { emit(PagingData.from(searchResultData)) })
        viewModel.setQuery(searchQuery)

        viewModel.getSearchResult().observeForever {
            assertNotNull(it)
        }
        verify(useCase, times(1)).findDataByQuery(searchQuery)
    }
}
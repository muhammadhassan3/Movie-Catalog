package com.dicoding.moviecatalog.ui.view.favorite.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import androidx.paging.PagingData
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.usecase.MovieFavoriteUseCase
import com.dicoding.moviecatalog.data.usecase.SeriesFavoriteUseCase
import com.dicoding.moviecatalog.utils.LiveDataTestUtil.value
import com.dicoding.moviecatalog.utils.QueryCollection
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
class FavoriteMoviesViewModelTest {

    @get:Rule
    val instantRunExecutor = InstantTaskExecutorRule()

    private val useCase = mock<MovieFavoriteUseCase>()
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: FavoriteMoviesViewModel
    private val movieData = FavoriteEntity(1, 1, "Title Favorite","", QueryCollection.MOVIE, "", "")

    @Before
    fun setUp() {
        viewModel = FavoriteMoviesViewModel(useCase)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getSize() {
        whenever(useCase.getMoviesSize()).thenReturn(liveData { emit(1) })

        val result = viewModel.getSize().value()
        verify(useCase).getMoviesSize()

        assertNotNull(result)
        assertEquals(1, result)
    }

    @Test
    fun getData() {
        whenever(useCase.getMoviesList()).thenReturn(liveData {
            emit(PagingData.from(listOf(movieData)))
        })

        viewModel.getData()
        verify(useCase, times(1)).getMoviesList()

        viewModel.getData().observeForever {
            assertNotNull(it)
        }
    }
}
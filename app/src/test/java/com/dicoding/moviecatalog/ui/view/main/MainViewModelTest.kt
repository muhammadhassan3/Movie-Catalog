package com.dicoding.moviecatalog.ui.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.moviecatalog.adapter.FilmMediumListAdapter
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.data.usecase.FilmListUseCase
import com.dicoding.moviecatalog.utils.*
import com.dicoding.moviecatalog.utils.Constant.NETWORK_PAGE_SIZE
import com.dicoding.moviecatalog.utils.DiffUtils.filmModelDiffUtils
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: FilmListUseCase

    private lateinit var viewModel: MainViewModel

    private val dispatcher = UnconfinedTestDispatcher()
    private val trendingList = providePopularListFromJson().results.map(RandomModelJson::asDomain)
    private val moviesList = provideMovieListFromJson().results.map(MovieModelJson::asDomain)
    private val seriesList = provideSeriesListFromJson().results.map(SeriesModelJson::asDomain)

    @Before
    fun setup() {
        viewModel = MainViewModel(useCase)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun shutdown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `get trending film from api`() = runTest(StandardTestDispatcher()){
        val result = flowOf(PagingData.from(trendingList))
        whenever(useCase.getPopularFilm()).thenReturn(result)

        viewModel.getTrending()
        verify(useCase, times(1)).getPopularFilm()

        viewModel.getTrending().collect{
            assertNotNull(it)
        }
    }

    @Test
    fun `get popular movie from api`() = runTest(StandardTestDispatcher()){
        val result = flowOf(PagingData.from(moviesList))

        whenever(useCase.getPopularMovies()).thenReturn(result)
        viewModel.getMovies()

        verify(useCase).getPopularMovies()

        viewModel.getMovies().collect{
            assertNotNull(it)
        }
    }

    @Test
    fun `get popular series from api and return success`() = runTest{
        val result = flowOf(PagingData.from(seriesList))

        whenever(useCase.getPopularSeries()).thenReturn(result)
        viewModel.getSeries()
        verify(useCase).getPopularSeries()

        viewModel.getSeries().collect{
            assertNotNull(it)
        }
    }
}
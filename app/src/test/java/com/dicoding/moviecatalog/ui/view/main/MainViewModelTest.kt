package com.dicoding.moviecatalog.ui.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.data.usecase.FilmListUseCase
import com.dicoding.moviecatalog.utils.*
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: FilmListUseCase

    @Mock
    private lateinit var observer: Observer<ApiResponse<List<FilmModel>>>

    private lateinit var viewModel: MainViewModel

    private val errorCode = 100
    private val errorMessage = "foo"
    private val requestErrorMessage = provideRequestErrorMessage(errorCode, errorMessage)

    private val trendingList = providePopularListFromJson().results.map(RandomModelJson::asDomain).chunked(randomCount)[0]
    private val moviesList = provideMovieListFromJson().results.map(MovieModelJson::asDomain)
    private val seriesList = provideSeriesListFromJson().results.map(SeriesModelJson::asDomain)

    @Before
    fun setup() {
        viewModel = MainViewModel(useCase)
    }

    @Test
    fun `get trending film from api and return success`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.success(trendingList, null)

        `when`(useCase.getPopularFilm(randomCount)).thenReturn(result)
        viewModel.setRandom(randomCount)

        val response = viewModel.random.value
        verify(useCase).getPopularFilm(randomCount)

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.SUCCESS)
        assertNotNull("Data is null", response?.data)
        assertEquals(randomCount, response?.data?.size)

        viewModel.random.observeForever(observer)
        verify(observer).onChanged(ApiResponse.success(trendingList, null))
    }

    @Test
    fun `get trending film from api and return no data`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.noData()

        `when`(useCase.getPopularFilm(randomCount)).thenReturn(result)
        viewModel.setRandom(randomCount)

        val response = viewModel.random.value
        verify(useCase).getPopularFilm(randomCount)

        assertNotNull("Response is null",response)
        assertEquals(response?.status, Status.NO_DATA)

        viewModel.random.observeForever(observer)
        verify(observer).onChanged(ApiResponse.noData())
    }

    @Test
    fun `get trending film from api and return request error`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.error(requestErrorMessage)

        `when`(useCase.getPopularFilm(randomCount)).thenReturn(result)
        viewModel.setRandom(randomCount)

        val response = viewModel.random.value
        verify(useCase).getPopularFilm(randomCount)

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)

        val message = response?.message?.getContentIfNotHandled()
        assertNotNull(message)
        assertEquals(message, requestErrorMessage)

        viewModel.random.observeForever(observer)
        verify(observer).onChanged(result.value)
    }

    @Test
    fun `get trending film from api and return connection error`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.error(errorMessage)

        `when`(useCase.getPopularFilm(randomCount)).thenReturn(result)
        viewModel.setRandom(randomCount)

        val response = viewModel.random.value
        verify(useCase).getPopularFilm(randomCount)

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)

        val message = response?.message?.getContentIfNotHandled()
        assertNotNull(message)
        assertEquals(message, errorMessage)

        viewModel.random.observeForever(observer)
        verify(observer).onChanged(result.value)
    }

    @Test
    fun `get popular movie from api and return success`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.success(moviesList, null)

        `when`(useCase.getPopularMovies()).thenReturn(result)
        viewModel.setMovies()

        val response = viewModel.movies.value
        verify(useCase).getPopularMovies()

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.SUCCESS)
        assertNotNull("Data is null", response?.data)
        assertEquals(moviesCount, response?.data?.size)

        viewModel.movies.observeForever(observer)
        verify(observer).onChanged(ApiResponse.success(moviesList, null))
    }

    @Test
    fun `get popular movie from api and return no data`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.noData()

        `when`(useCase.getPopularMovies()).thenReturn(result)
        viewModel.setMovies()

        val response = viewModel.movies.value
        verify(useCase).getPopularMovies()

        assertNotNull("Response is null",response)
        assertEquals(response?.status, Status.NO_DATA)

        viewModel.movies.observeForever(observer)
        verify(observer).onChanged(ApiResponse.noData())
    }

    @Test
    fun `get popular movie from api and return request error`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.error(requestErrorMessage)

        `when`(useCase.getPopularMovies()).thenReturn(result)
        viewModel.setMovies()

        val response = viewModel.movies.value
        verify(useCase).getPopularMovies()

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)

        val message = response?.message?.getContentIfNotHandled()
        assertNotNull(message)
        assertEquals(message, requestErrorMessage)

        viewModel.movies.observeForever(observer)
        verify(observer).onChanged(result.value)
    }

    @Test
    fun `get popular movie from api and return connection error`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.error(errorMessage)

        `when`(useCase.getPopularMovies()).thenReturn(result)
        viewModel.setMovies()

        val response = viewModel.movies.value
        verify(useCase).getPopularMovies()

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)

        val message = response?.message?.getContentIfNotHandled()
        assertNotNull(message)
        assertEquals(message, errorMessage)

        viewModel.movies.observeForever(observer)
        verify(observer).onChanged(result.value)
    }

    @Test
    fun `get popular series from api and return success`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.success(seriesList, null)

        `when`(useCase.getPopularSeries()).thenReturn(result)
        viewModel.setSeries()

        val response = viewModel.series.value
        verify(useCase).getPopularSeries()

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.SUCCESS)
        assertNotNull("Data is null", response?.data)
        assertEquals(seriesCount, response?.data?.size)

        viewModel.series.observeForever(observer)
        verify(observer).onChanged(ApiResponse.success(seriesList, null))
    }

    @Test
    fun `get popular series from api and return no data`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.noData()

        `when`(useCase.getPopularSeries()).thenReturn(result)
        viewModel.setSeries()

        val response = viewModel.series.value
        verify(useCase).getPopularSeries()

        assertNotNull("Response is null",response)
        assertEquals(response?.status, Status.NO_DATA)

        viewModel.series.observeForever(observer)
        verify(observer).onChanged(ApiResponse.noData())
    }

    @Test
    fun `get popular series from api and return request error`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.error(requestErrorMessage)

        `when`(useCase.getPopularSeries()).thenReturn(result)
        viewModel.setSeries()

        val response = viewModel.series.value
        verify(useCase).getPopularSeries()

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)

        val message = response?.message?.getContentIfNotHandled()
        assertNotNull(message)
        assertEquals(message, requestErrorMessage)

        viewModel.series.observeForever(observer)
        verify(observer).onChanged(result.value)
    }

    @Test
    fun `get popular series from api and return connection error`() {
        val result = MutableLiveData<ApiResponse<List<FilmModel>>>()
        result.value = ApiResponse.error(errorMessage)

        `when`(useCase.getPopularSeries()).thenReturn(result)
        viewModel.setSeries()

        val response = viewModel.series.value
        verify(useCase).getPopularSeries()

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)

        val message = response?.message?.getContentIfNotHandled()
        assertNotNull(message)
        assertEquals(message, errorMessage)

        viewModel.series.observeForever(observer)
        verify(observer).onChanged(result.value)
    }
}
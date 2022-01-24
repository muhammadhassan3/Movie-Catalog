package com.dicoding.moviecatalog.ui.view.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.usecase.FilmDetailUseCase
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
class DetailFilmViewModelTest {
    private lateinit var viewModel: DetailFilmViewModel
    private val dummyMovies = provideMovieDummyData().asDomain()
    private val dummySeries = provideSeriesDummyData().asDomain()

    private val movieId = dummyMovies.id
    private val seriesId = dummySeries.id

    private val errorCode = 100
    private val errorMessage = "foo"
    private val requestErrorMessage = provideRequestErrorMessage(errorCode, errorMessage)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: FilmDetailUseCase

    @Mock
    private lateinit var observer: Observer<ApiResponse<FilmModel>>

    @Before
    fun setup(){
        viewModel = DetailFilmViewModel(useCase)
    }

    @Test
    fun `get movies detail from api and return success`() {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        result.value = ApiResponse.success(dummyMovies, null)

        `when`(useCase.getDetailMovie(movieId)).thenReturn(result)
        viewModel.setData(movieId, "movie")
        val response = viewModel.data.value
        verify(useCase).getDetailMovie(movieId)
        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.SUCCESS)
        assertNotNull("Data is null", response?.data)
        val data = response!!.data!!

        assertNotNull("Title is null", data.title)
        assertEquals(data.title, dummyMovies.title)
        assertNotNull("Rating is null", data.rating)
        assertEquals(data.rating, dummyMovies.rating)
        assertNotNull("Release date is null", data.releaseDate)
        assertEquals(data.releaseDate, dummyMovies.releaseDate)
        assertNotNull("Genres is null", data.genres)
        assertEquals(data.genres.size, dummyMovies.genres.size)
        assertNotNull("Synopsis is null", data.synopsis)
        assertEquals(data.synopsis, dummyMovies.synopsis)
        assertNotNull("Poster path is null", data.poster)
        assertEquals(data.poster, dummyMovies.poster)
        assertNotNull("Id is null", data.id)
        assertEquals(data.id, dummyMovies.id)

        viewModel.data.observeForever(observer)
        verify(observer).onChanged(ApiResponse.success(dummyMovies, null))
    }

    @Test
    fun `get detail movies from api and return no data`() {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        result.value = ApiResponse.noData()

        `when`(useCase.getDetailMovie(movieId)).thenReturn(result)
        viewModel.setData(movieId, "movie")
        val response = viewModel.data.value
        verify(useCase).getDetailMovie(movieId)
        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.NO_DATA)

        viewModel.data.observeForever(observer)
        verify(observer).onChanged(ApiResponse.noData())
    }

    @Test
    fun `get detail movies from api and return request error`() {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        result.value = ApiResponse.error(requestErrorMessage)

        `when`(useCase.getDetailMovie(movieId)).thenReturn(result)
        viewModel.setData(movieId, "movie")

        val response = viewModel.data.value
        verify(useCase).getDetailMovie(movieId)

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)
        val message = response?.message?.getContentIfNotHandled()
        assertNotNull("Message is null", message)
        assertEquals(message, requestErrorMessage)

        viewModel.data.observeForever(observer)
        verify(observer).onChanged(result.value)
    }

    @Test
    fun `get movies detail from api and return connection error`() {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        result.value = ApiResponse.error(errorMessage)

        `when`(useCase.getDetailMovie(movieId)).thenReturn(result)
        viewModel.setData(movieId, "movie")

        val response = viewModel.data.value
        verify(useCase).getDetailMovie(movieId)

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)
        val message = response?.message?.getContentIfNotHandled()
        assertNotNull("Message is null", message)
        assertEquals(message, errorMessage)

        viewModel.data.observeForever(observer)
        verify(observer).onChanged(result.value)
    }

    @Test
    fun `get series detail from api and return success`() {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        result.value = ApiResponse.success(dummySeries, null)

        `when`(useCase.getDetailSeries(seriesId)).thenReturn(result)
        viewModel.setData(seriesId, "tv")
        val response = viewModel.data.value
        verify(useCase).getDetailSeries(seriesId)
        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.SUCCESS)
        assertNotNull("Data is null", response?.data)
        val data = response!!.data!!

        assertNotNull("Title is null", data.title)
        assertEquals(data.title, dummySeries.title)
        assertNotNull("Rating is null", data.rating)
        assertEquals(data.rating, dummySeries.rating)
        assertNotNull("Release date is null", data.releaseDate)
        assertEquals(data.releaseDate, dummySeries.releaseDate)
        assertNotNull("Genres is null", data.genres)
        assertEquals(data.genres.size, dummySeries.genres.size)
        assertNotNull("Synopsis is null", data.synopsis)
        assertEquals(data.synopsis, dummySeries.synopsis)
        assertNotNull("Poster path is null", data.poster)
        assertEquals(data.poster, dummySeries.poster)
        assertNotNull("Id is null", data.id)
        assertEquals(data.id, dummySeries.id)

        viewModel.data.observeForever(observer)
        verify(observer).onChanged(ApiResponse.success(dummySeries, null))
    }

    @Test
    fun `get series detail from api and return no data`() {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        result.value = ApiResponse.noData()

        `when`(useCase.getDetailSeries(seriesId)).thenReturn(result)
        viewModel.setData(seriesId, "tv")
        val response = viewModel.data.value
        verify(useCase).getDetailSeries(seriesId)
        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.NO_DATA)

        viewModel.data.observeForever(observer)
        verify(observer).onChanged(ApiResponse.noData())
    }

    @Test
    fun `get series detail from api and return request error`() {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        result.value = ApiResponse.error(requestErrorMessage)

        `when`(useCase.getDetailSeries(seriesId)).thenReturn(result)
        viewModel.setData(seriesId, "tv")

        val response = viewModel.data.value
        verify(useCase).getDetailSeries(seriesId)

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)
        val message = response?.message?.getContentIfNotHandled()
        assertNotNull("Message is null", message)
        assertEquals(message, requestErrorMessage)

        viewModel.data.observeForever(observer)
        verify(observer).onChanged(result.value)
    }

    @Test
    fun `get series detail from api and return connection error`() {
        val result = MutableLiveData<ApiResponse<FilmModel>>()
        result.value = ApiResponse.error(errorMessage)

        `when`(useCase.getDetailSeries(seriesId)).thenReturn(result)
        viewModel.setData(seriesId, "tv")

        val response = viewModel.data.value
        verify(useCase).getDetailSeries(seriesId)

        assertNotNull("Response is null", response)
        assertEquals(response?.status, Status.ERROR)
        val message = response?.message?.getContentIfNotHandled()
        assertNotNull("Message is null", message)
        assertEquals(message, errorMessage)

        viewModel.data.observeForever(observer)
        verify(observer).onChanged(result.value)
    }
}
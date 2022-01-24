package com.dicoding.moviecatalog.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.data.remote.RemoteDataSource
import com.dicoding.moviecatalog.utils.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class FilmRepositoryTest{

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private var repository = FakeFilmRepository(remote)

    private val movieData = provideMovieDummyData().asDomain()
    private val seriesData = provideSeriesDummyData().asDomain()

    private val errorCode = 100
    private val errorMessage = "foo"
    private val requestErrorMessage = provideRequestErrorMessage(errorCode, errorMessage)

    private val trendingData = providePopularListFromJson().results.map(RandomModelJson::asDomain).chunked(randomCount)[0]
    private val moviesListData = provideMovieListFromJson().results.map(MovieModelJson::asDomain)
    private val seriesListData = provideSeriesListFromJson().results.map(SeriesModelJson::asDomain)

    @Test
    fun `test get trending film from api and return success`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadTrendingList).onResponseSuccess(trendingData)
        }.`when`(remote).loadRandom(eq(randomCount), any())

        val response = LiveDataTestUtil.getValue(repository.loadRandom(randomCount))
        verify(remote).loadRandom(eq(randomCount), any())

        assertEquals(response.status, Status.SUCCESS)
        assertNotNull("Data is null", response.data)
        val data = response.data!!

        assertEquals(data.size, randomCount)
    }

    @Test
    fun `test get popular movies from api and return success`() {
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadMoviesList).onResponseSuccess(moviesListData)
        }.`when`(remote).loadMovies(any())

        val response = LiveDataTestUtil.getValue(repository.loadMovies())
        verify(remote).loadMovies(any())

        assertEquals(response.status, Status.SUCCESS)
        assertNotNull("Data is null", response.data)
        val data = response.data!!

        assertEquals(data.size, moviesCount)
    }

    @Test
    fun `test get popular series from api and return success`() {
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadSeriesList).onResponseSuccess(seriesListData)
        }.`when`(remote).loadSeries(any())

        val response = LiveDataTestUtil.getValue(repository.loadSeries())
        verify(remote).loadSeries(any())

        assertEquals(response.status, Status.SUCCESS)
        assertNotNull("Data is null", response.data)

        val data = response.data!!

        assertEquals(data.size, seriesCount)
    }

    @Test
    fun `test get trending film and return no data`(){
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadTrendingList).onNoDataReceived()
        }.`when`(remote).loadRandom(eq(randomCount), any())

        val response = LiveDataTestUtil.getValue(repository.loadRandom(randomCount))
        verify(remote).loadRandom(eq(randomCount), any())

        assertEquals(response.status, Status.NO_DATA)
    }

    @Test
    fun `test get popular movies and return no data`(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadMoviesList).onNoDataReceived()
        }.`when`(remote).loadMovies(any())

        val response = LiveDataTestUtil.getValue(repository.loadMovies())
        verify(remote).loadMovies(any())

        assertEquals(response.status, Status.NO_DATA)
    }

    @Test
    fun `test get popular series and return no data`(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadSeriesList).onNoDataReceived()
        }.`when`(remote).loadSeries(any())

        val response = LiveDataTestUtil.getValue(repository.loadSeries())
        verify(remote).loadSeries(any())

        assertEquals(response.status, Status.NO_DATA)
    }

    @Test
    fun `test get trending film and return request error`(){
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadTrendingList).onErrorResponse(errorCode, errorMessage)
        }.`when`(remote).loadRandom(eq(randomCount), any())

        val response = LiveDataTestUtil.getValue(repository.loadRandom(randomCount))
        verify(remote).loadRandom(eq(randomCount), any())

        assertEquals(response.status, Status.ERROR)
        val responseMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseMessage)

        assertEquals(responseMessage, requestErrorMessage)
    }

    @Test
    fun `test get popular movies and return request error`(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadMoviesList).onErrorResponse(errorCode, errorMessage)
        }.`when`(remote).loadMovies(any())

        val response = LiveDataTestUtil.getValue(repository.loadMovies())
        verify(remote).loadMovies(any())

        assertEquals(response.status, Status.ERROR)
        val responseMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseMessage)

        assertEquals(responseMessage, requestErrorMessage)
    }

    @Test
    fun `test get popular series and return request error`(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadSeriesList).onErrorResponse(errorCode, errorMessage)
        }.`when`(remote).loadSeries(any())

        val response = LiveDataTestUtil.getValue(repository.loadSeries())
        verify(remote).loadSeries(any())

        assertEquals(response.status, Status.ERROR)
        val responseMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseMessage)

        assertEquals(responseMessage, requestErrorMessage)
    }

    @Test
    fun `test get trending films and return connection error`(){
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadTrendingList).onConnectionError(errorMessage)
        }.`when`(remote).loadRandom(eq(randomCount), any())

        val response = LiveDataTestUtil.getValue(repository.loadRandom(randomCount))
        verify(remote).loadRandom(eq(randomCount), any())

        assertEquals(response.status, Status.ERROR)
        val responseMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseMessage)
        assertEquals(responseMessage, errorMessage)
    }

    @Test
    fun `test get popular movies and return connection error`(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadMoviesList).onConnectionError(errorMessage)
        }.`when`(remote).loadMovies(any())

        val response = LiveDataTestUtil.getValue(repository.loadMovies())
        verify(remote).loadMovies(any())

        assertEquals(response.status, Status.ERROR)
        val responseMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseMessage)
        assertEquals(responseMessage, errorMessage)
    }

    @Test
    fun `test get popular series and return connection error`(){
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadSeriesList).onConnectionError(errorMessage)
        }.`when`(remote).loadSeries(any())

        val response = LiveDataTestUtil.getValue(repository.loadSeries())
        verify(remote).loadSeries(any())

        assertEquals(response.status, Status.ERROR)
        val responseMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseMessage)
        assertEquals(responseMessage, errorMessage)
    }

    @Test
    fun `test get movies detail from api and return success`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadMovieDetail).onResponseSuccess(
                movieData
            )
        }.`when`(remote).loadMoviesDetail(eq(movieData.id), any())


        val response = LiveDataTestUtil.getValue(repository.loadMoviesDetail(movieData.id))
        verify(remote).loadMoviesDetail(eq(movieData.id), any())

        assertEquals(response.status, Status.SUCCESS)
        assertNotNull("Data is null", response.data)
        val data = response.data!!

        assertNotNull("Title is null", data.title)
        assertEquals(data.title, movieDummy.title)
        assertNotNull("Rating is null", data.rating)
        assertEquals(data.rating, movieDummy.rating)
        assertNotNull("Release date is null", data.releaseDate)
        assertEquals(data.releaseDate, movieDummy.releaseDate)
        assertNotNull("Genres is null", data.genres)
        assertEquals(data.genres.size, movieDummy.genres.size)
        assertNotNull("Synopsis is null", data.synopsis)
        assertEquals(data.synopsis, movieDummy.synopsis)
        assertNotNull("Poster path is null", data.poster)
        assertEquals(data.poster, movieDummy.poster)
        assertNotNull("Id is null", data.id)
        assertEquals(data.id, movieDummy.id)
    }

    @Test
    fun `test get series detail from api and return success`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadSeriesDetail).onResponseSuccess(
                seriesData
            )
        }.`when`(remote).loadSeriesDetail(eq(seriesData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadSeriesDetail(seriesDummy.id))
        verify(remote).loadSeriesDetail(eq(seriesData.id), any())

        assertEquals(response.status, Status.SUCCESS)
        assertNotNull("Data is null", response.data)
        val data = response.data!!

        assertNotNull("Title is null", data.title)
        assertEquals(data.title, seriesDummy.title)
        assertNotNull("Rating is null", data.rating)
        assertEquals(data.rating, seriesDummy.rating)
        assertNotNull("Release date is null", data.releaseDate)
        assertEquals(data.releaseDate, seriesDummy.releaseDate)
        assertNotNull("Genres is null", data.genres)
        assertEquals(data.genres.size, seriesDummy.genres.size)
        assertNotNull("Synopsis is null", data.synopsis)
        assertEquals(data.synopsis, seriesDummy.synopsis)
        assertNotNull("Poster path is null", data.poster)
        assertEquals(data.poster, seriesDummy.poster)
        assertNotNull("Id is null", data.id)
        assertEquals(data.id, seriesDummy.id)
    }

    @Test
    fun `test get detail movies from api and return no data`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadMovieDetail).onNoDataReceived()
        }.`when`(remote).loadMoviesDetail(eq(movieData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadMoviesDetail(movieData.id))
        verify(remote).loadMoviesDetail(eq(movieData.id), any())

        assertEquals(response.status, Status.NO_DATA)
    }

    @Test
    fun `test get detail series from api and return no data`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadSeriesDetail).onNoDataReceived()
        }.`when`(remote).loadSeriesDetail(eq(seriesData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadSeriesDetail(seriesData.id))
        verify(remote).loadSeriesDetail(eq(seriesData.id), any())

        assertEquals(response.status, Status.NO_DATA)
    }

    @Test
    fun `test get detail movies from api and return request error`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadMovieDetail).onErrorResponse(
                errorCode,
                errorMessage
            )
        }.`when`(remote).loadMoviesDetail(eq(movieData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadMoviesDetail(movieData.id))
        verify(remote).loadMoviesDetail(eq(movieData.id), any())

        assertEquals(response.status, Status.ERROR)

        val responseErrorMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseErrorMessage)
        assertEquals(responseErrorMessage, requestErrorMessage)
    }

    @Test
    fun `test get detail series from api and return request error`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadSeriesDetail).onErrorResponse(
                errorCode,
                errorMessage
            )
        }.`when`(remote).loadSeriesDetail(eq(seriesData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadSeriesDetail(seriesData.id))
        verify(remote).loadSeriesDetail(eq(seriesData.id), any())

        assertEquals(response.status, Status.ERROR)
        val responseErrorMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseErrorMessage)
        assertEquals(responseErrorMessage, requestErrorMessage)
    }

    @Test
    fun `test get detail movies from api and return connection error`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadMovieDetail).onConnectionError(
                errorMessage
            )
        }.`when`(remote).loadMoviesDetail(eq(movieData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadMoviesDetail(movieData.id))
        verify(remote).loadMoviesDetail(eq(movieData.id), any())

        assertEquals(response.status, Status.ERROR)

        val responseErrorMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseErrorMessage)
        assertEquals(responseErrorMessage, errorMessage)
    }

    @Test
    fun `test get detail series from api and return connection error`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadSeriesDetail).onConnectionError(
                errorMessage
            )
        }.`when`(remote).loadSeriesDetail(eq(seriesData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadSeriesDetail(seriesData.id))
        verify(remote).loadSeriesDetail(eq(seriesData.id), any())

        assertEquals(response.status, Status.ERROR)
        val responseErrorMessage = response.message?.getContentIfNotHandled()
        assertNotNull("Message is null", responseErrorMessage)
        assertEquals(responseErrorMessage, errorMessage)
    }
}
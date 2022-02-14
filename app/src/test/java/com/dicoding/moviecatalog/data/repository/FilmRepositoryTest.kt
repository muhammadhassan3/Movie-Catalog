package com.dicoding.moviecatalog.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.dicoding.moviecatalog.data.datasource.RemoteDataSource
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.data.pagingsource.MoviesPagingSource
import com.dicoding.moviecatalog.data.pagingsource.SearchPagingSource
import com.dicoding.moviecatalog.data.pagingsource.SeriesPagingSource
import com.dicoding.moviecatalog.data.pagingsource.TrendingPagingSource
import com.dicoding.moviecatalog.utils.*
import com.dicoding.moviecatalog.utils.Constant.TMDB_STARTING_PAGE
import com.dicoding.moviecatalog.utils.exception.NoDataAvailableException
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class FilmRepositoryTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private lateinit var repository: FakeFilmRepository

    private val movieData = provideMovieDummyData().asDomain()
    private val seriesData = provideSeriesDummyData().asDomain()

    private val page = TMDB_STARTING_PAGE

    private val errorCode = 100
    private val errorMessage = "foo"
    private val requestErrorMessage = provideRequestErrorMessage(errorCode, errorMessage)
    private val searchQuery = "spiderman"

    private val trendingData = providePopularListFromJson().results.map(RandomModelJson::asDomain)
    private val moviesListData = provideMovieListFromJson().results.map(MovieModelJson::asDomain)
    private val seriesListData = provideSeriesListFromJson().results.map(SeriesModelJson::asDomain)
    private val searchResultData = provideSearchResultData().results.map(RandomModelJson::asDomain)

    @Before
    fun before() {
        repository = FakeFilmRepository(remote)
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `test get trending film from api and return success`() {
        runTest {
            val trendingPagingSource = TrendingPagingSource(remote)
            whenever(remote.loadTrending(page)).thenReturn(
                ApiResponse.success(
                    trendingData,
                    null
                )
            )
            val actual = trendingPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected = PagingSource.LoadResult.Page(
                data = trendingData,
                prevKey = null,
                nextKey = 2
            )
            verify(remote, times(1)).loadTrending(page)
            assertEquals(
                expected, actual
            )
        }
    }

    @Test
    fun `test get popular movies from api and return success`() {
        runTest {
            val moviesSource = MoviesPagingSource(remote)
            whenever(remote.loadMovies(page)).thenReturn(
                ApiResponse.success(moviesListData, null)
            )
            val actual = moviesSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )
            val expected = PagingSource.LoadResult.Page(
                data = moviesListData,
                prevKey = null,
                nextKey = 2
            )
            verify(remote, times(1)).loadMovies(page)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test get popular series from api and return success`() {
        runTest {
            val seriesSource = SeriesPagingSource(remote)
            whenever(remote.loadSeries(page)).thenReturn(
                ApiResponse.success(seriesListData, null)
            )
            val actual = seriesSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )
            val expected = PagingSource.LoadResult.Page(
                data = seriesListData,
                prevKey = null,
                nextKey = 2
            )

            verify(remote, times(1)).loadSeries(page)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test get trending film and return no data`() {
        runTest {
            val pagingSource = TrendingPagingSource(remote)
            whenever(remote.loadTrending(page)).thenReturn(
                ApiResponse.noData()
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected =
                PagingSource.LoadResult.Error<Int, FilmModel>(throwable = NoDataAvailableException())
            verify(remote, times(1)).loadTrending(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get popular movies and return no data`() {
        runTest {
            val pagingSource = MoviesPagingSource(remote)
            whenever(remote.loadMovies(page)).thenReturn(
                ApiResponse.noData()
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected =
                PagingSource.LoadResult.Error<Int, FilmModel>(throwable = NoDataAvailableException())
            verify(remote, times(1)).loadMovies(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get popular series and return no data`() {
        runTest {
            val pagingSource = SeriesPagingSource(remote)
            whenever(remote.loadSeries(page)).thenReturn(
                ApiResponse.noData()
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected =
                PagingSource.LoadResult.Error<Int, FilmModel>(throwable = NoDataAvailableException())
            verify(remote, times(1)).loadSeries(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get trending film and return request error`() {
        runTest {
            val pagingSource = TrendingPagingSource(remote)
            whenever(remote.loadTrending(page)).thenReturn(
                ApiResponse.error(requestErrorMessage)
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected = PagingSource.LoadResult.Error<Int, FilmModel>(
                throwable = IllegalStateException(requestErrorMessage)
            )
            verify(remote, times(1)).loadTrending(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get popular movies and return request error`() {
        runTest {
            val pagingSource = MoviesPagingSource(remote)
            whenever(remote.loadMovies(page)).thenReturn(
                ApiResponse.error(requestErrorMessage)
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected = PagingSource.LoadResult.Error<Int, FilmModel>(
                throwable = IllegalStateException(requestErrorMessage)
            )
            verify(remote, times(1)).loadMovies(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get popular series and return request error`() {
        runTest {
            val pagingSource = SeriesPagingSource(remote)
            whenever(remote.loadSeries(page)).thenReturn(
                ApiResponse.error(requestErrorMessage)
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected = PagingSource.LoadResult.Error<Int, FilmModel>(
                throwable = IllegalStateException(requestErrorMessage)
            )
            verify(remote, times(1)).loadSeries(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get trending films and return connection error`() {
        runTest {
            val pagingSource = TrendingPagingSource(remote)
            whenever(remote.loadTrending(page)).thenThrow(
                RuntimeException()
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected =
                PagingSource.LoadResult.Error<Int, FilmModel>(throwable = RuntimeException())
            verify(remote, times(1)).loadTrending(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get popular movies and return connection error`() {
        runTest {
            val pagingSource = MoviesPagingSource(remote)
            whenever(remote.loadMovies(page)).thenThrow(
                RuntimeException()
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected =
                PagingSource.LoadResult.Error<Int, FilmModel>(throwable = RuntimeException())
            verify(remote, times(1)).loadMovies(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get popular series and return connection error`() {
        runTest {
            val pagingSource = SeriesPagingSource(remote)
            whenever(remote.loadSeries(page)).thenThrow(
                RuntimeException()
            )

            val actual = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected =
                PagingSource.LoadResult.Error<Int, FilmModel>(throwable = RuntimeException())
            verify(remote, times(1)).loadSeries(page)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test search film by query from api and return success`() = runTest {
        val source = SearchPagingSource(remote, searchQuery)
        whenever(remote.getSearchResult(page, searchQuery)).thenReturn(
            ApiResponse.success(searchResultData, null)
        )

        val actual = source.load(
            PagingSource.LoadParams.Refresh(
                loadSize = 1,
                placeholdersEnabled = false,
                key = null
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = searchResultData,
            prevKey = null,
            nextKey = 2
        )
        verify(remote,times(1)).getSearchResult(page, searchQuery)

        assertEquals(expected, actual)
    }

    @Test
    fun `test search film by query from api and return no data`() {
        runTest {
            val source = SearchPagingSource(remote, searchQuery)
            whenever(remote.getSearchResult(page, searchQuery)).thenReturn(
                ApiResponse.noData()
            )

            val actual = source.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected =
                PagingSource.LoadResult.Error<Int, FilmModel>(throwable = NoDataAvailableException())
            verify(remote, times(1)).getSearchResult(page, searchQuery)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `search film by query from api and return request error`() {
        runTest {
            val source = SearchPagingSource(remote, searchQuery)
            whenever(remote.getSearchResult(page, searchQuery)).thenReturn(
                ApiResponse.error(requestErrorMessage)
            )

            val actual = source.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected = PagingSource.LoadResult.Error<Int, FilmModel>(
                throwable = IllegalStateException(requestErrorMessage)
            )
            verify(remote, times(1)).getSearchResult(page, searchQuery)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `search film by query from api and return connection error`() {
        runTest {
            val source = SearchPagingSource(remote, searchQuery)
            whenever(remote.getSearchResult(page, searchQuery)).thenThrow(
                RuntimeException()
            )

            val actual = source.load(
                PagingSource.LoadParams.Refresh(
                    loadSize = 1,
                    placeholdersEnabled = false,
                    key = null
                )
            )

            val expected =
                PagingSource.LoadResult.Error<Int, FilmModel>(throwable = RuntimeException())
            verify(remote, times(1)).getSearchResult(page, searchQuery)

            assert(actual is PagingSource.LoadResult.Error)
            assert(actual.toString() == expected.toString())
        }
    }

    @Test
    fun `test get movies detail from api and return success`() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadMovieDetail).onResponseSuccess(
                movieData
            )
        }.`when`(remote).loadMovieDetail(eq(movieData.id), any())


        val response = LiveDataTestUtil.getValue(repository.loadMoviesDetail(movieData.id))
        verify(remote).loadMovieDetail(eq(movieData.id), any())

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
        }.`when`(remote).loadMovieDetail(eq(movieData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadMoviesDetail(movieData.id))
        verify(remote).loadMovieDetail(eq(movieData.id), any())

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
        }.`when`(remote).loadMovieDetail(eq(movieData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadMoviesDetail(movieData.id))
        verify(remote).loadMovieDetail(eq(movieData.id), any())

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
        }.`when`(remote).loadMovieDetail(eq(movieData.id), any())

        val response = LiveDataTestUtil.getValue(repository.loadMoviesDetail(movieData.id))
        verify(remote).loadMovieDetail(eq(movieData.id), any())

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
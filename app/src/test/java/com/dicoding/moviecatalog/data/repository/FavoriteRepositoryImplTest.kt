package com.dicoding.moviecatalog.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.datasource.LocalDataSource
import com.dicoding.moviecatalog.utils.LiveDataTestUtil
import com.dicoding.moviecatalog.utils.LiveDataTestUtil.value
import com.dicoding.moviecatalog.utils.QueryCollection
import com.dicoding.moviecatalog.utils.QueryCollection.MOVIE
import com.dicoding.moviecatalog.utils.QueryCollection.TV_SHOW
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private var local = mock<LocalDataSource>()
    private lateinit var repository: FakeFavoriteRepository

    private val data = FavoriteEntity(1L, 0, "Title", "", MOVIE,"","")
    private val seriesData = FavoriteEntity(2L, 0, "Title2", "", TV_SHOW,"","")

    @Before
    fun setUp() {
        repository = FakeFavoriteRepository(local)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insert() = runTest{
        repository.insert(data)
        verify(local, times(1)).insert(data)
    }

    @Test
    fun `get favorite data and return movie list`() {
        whenever(local.getFavoriteData(MOVIE)).thenReturn(
            liveData {emit(PagingData.from(listOf(data)))}
        )

        repository.getFavoriteData(MOVIE).observeForever {
            assertNotNull(it)
        }
        verify(local).getFavoriteData(MOVIE)
    }

    @Test
    fun `get favorite data and return series list`() {
        whenever(local.getFavoriteData(TV_SHOW)).thenReturn(
            liveData {emit(PagingData.from(listOf(seriesData)))}
        )

        repository.getFavoriteData(TV_SHOW).observeForever {
            assertNotNull(it)
        }
        verify(local).getFavoriteData(TV_SHOW)
    }

    @Test
    fun `get favorite data count`() {
        whenever(local.getFavoriteDataSize(MOVIE)).thenReturn(liveData { emit(1) })

        val result = repository.getFavoriteDataCount(MOVIE).value()
        verify(local).getFavoriteDataSize(MOVIE)

        assertNotNull(result)
        assertEquals(1, result)
    }

    @Test
    fun `get favorite by id`() {
        whenever(local.getFavoriteById(1)).thenReturn(liveData { emit(data) })

        val result = repository.getFavoriteById(1).value()
        verify(local).getFavoriteById(1)

        assertNotNull(result)
        assertEquals(data, result)
    }

    @Test
    fun `delete favorite by id`() {
        repository.deleteFavoriteById(data.id ?: 1)
        verify(local).deleteFavoriteById(data.id ?: 1)
    }
}
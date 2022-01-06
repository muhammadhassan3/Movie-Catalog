package com.dicoding.moviecatalog.ui.view.main

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private val moviesCount = 19
    private val seriesCount = 20
    private val randomCount = 10

    @Before
    fun setup() {
        viewModel = MainViewModel()
        viewModel.setRandom(randomCount)
        viewModel.setMovies()
        viewModel.setSeries()
    }

    @Test
    fun testGetRandom() {
        val randomList = viewModel.random.value
        assertNotNull("randomList is null", randomList)
        assertEquals(randomCount, randomList?.size)
    }

    @Test
    fun testGetMovies() {
        val movieList = viewModel.movies.value
        assertNotNull("movieList is null", movieList)
        assertEquals(moviesCount, movieList?.size)
    }

    @Test
    fun testGetSeries() {
        val seriesList = viewModel.series.value
        assertNotNull("seriesList is null", seriesList)
        assertEquals(seriesCount, seriesList?.size)
    }
}
package com.dicoding.moviecatalog.ui.view.detail

import com.dicoding.moviecatalog.data.model.DummyData
import com.dicoding.moviecatalog.data.model.FilmModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DetailFilmViewModelTest {
    private lateinit var viewModel: DetailFilmViewModel
    private val dummyData = DummyData()
    private val list: List<FilmModel> = dummyData.getData()

    @Before
    fun setup() {
        viewModel = DetailFilmViewModel()
        viewModel.setData(list[0])
    }

    @Test
    fun testGetData() {
        val detail = viewModel.data.value
        assertNotNull("Detail is null", detail)
        assertNotNull("Title is null", detail?.title)
        assertEquals(detail?.title, list[0].title)
        assertNotNull("Genres is null", detail?.genres)
        assertEquals(detail?.genres?.size, list[0].genres.size)
        assertNotNull("Poster is null", detail?.poster)
        assertEquals(detail?.poster, list[0].poster)
        assertNotNull("Rating is null", detail?.rating)
        assertEquals(detail?.rating, list[0].rating)
        assertNotNull("Release Date is null", detail?.releaseDate)
        assertEquals(detail?.releaseDate, list[0].releaseDate)
        assertNotNull("Synopsis is null", detail?.synopsis)
        assertEquals(detail?.synopsis, list[0].synopsis)
        assertNotNull("Film Type is null", detail?.type)
        assertEquals(detail?.type, list[0].type)
    }
}
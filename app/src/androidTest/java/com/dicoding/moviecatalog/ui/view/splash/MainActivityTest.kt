package com.dicoding.moviecatalog.ui.view.splash

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.dicoding.moviecatalog.ui.view.main.MainActivity
import com.dicoding.moviecatalog.utils.*
import com.dicoding.moviecatalog.utils.CustomMatches.withRating
import com.dicoding.moviecatalog.utils.CustomMatches.withTitle
import com.dicoding.moviecatalog.utils.rest.RetrofitClient
import kotlinx.serialization.ExperimentalSerializationApi
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalSerializationApi
class MainActivityTest {
    private val position = 0

    private val api = RetrofitClient.getFilmApi()
    private lateinit var movieApi: List<FilmModel>
    private lateinit var trendingApi: List<FilmModel>
    private lateinit var seriesApi: List<FilmModel>

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResources)
        val movieRequest = api.getMovies()
        val seriesRequest = api.getSeries()
        val trendingRequest = api.getTrending()
        try{
            val movieResponse = movieRequest.execute()
            val seriesResponse = seriesRequest.execute()
            val trendingRespone = trendingRequest.execute()
            if(movieResponse.isSuccessful){
                movieResponse.body()?.results?.let{
                    movieApi = it.map(MovieModelJson::asDomain)
                }
            }

            if(seriesResponse.isSuccessful)
                seriesResponse.body()?.results?.let{
                    seriesApi = it.map(SeriesModelJson::asDomain)
                }

            if(trendingRespone.isSuccessful)
                trendingRespone.body()?.results?.let{
                    trendingApi = it.map(RandomModelJson::asDomain)
                }
        }catch (e: Exception){
            throw IllegalArgumentException("Dummy data initialization error")
        }
    }

    @After
    fun clear() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResources)
    }

    @Test
    fun testMainView() {
        onView(withId(R.id.tvTagRecommendation)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTagRecommendation)).check(matches(withText("Rekomendasi")))

        onView(withId(R.id.tabLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.viewPager)).check(matches(isDisplayed()))
    }

    @Test
    fun testRandomList() {
        onView(withId(R.id.rvRandom)).check(matches(isDisplayed()))
        onView(withId(R.id.rvRandom)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                trendingApi.size
            )
        )
    }

    @Test
    fun testMovies() {
        onView(withText("MOVIE")).check(matches(isDisplayed()))
        onView(withText("MOVIE")).perform(click())
        onView(withId(R.id.rvMovies)).check(matches(isDisplayed()))
        onView(withId(R.id.rvMovies)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                movieApi.size
            )
        )
    }

    @Test
    fun testTvShow() {
        onView(withText("TV SHOW")).check(matches(isDisplayed()))
        onView(withText("TV SHOW")).perform(click())
        onView(withId(R.id.rvTvShow)).check(matches(isDisplayed()))
        onView(withId(R.id.rvTvShow)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                seriesApi.size
            )
        )
    }

    @Test
    fun testDetailFilmFromRandom() {
        val item = trendingApi[position]
        onView(withId(R.id.rvRandom)).check(matches(isDisplayed()))
        onView(withId(R.id.rvRandom)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                click()
            )
        )
        checkDetail(item, item.type)
    }

    @Test
    fun testDetailFilmFromMovieTab() {
        val item = movieApi[position]
        onView(withText("MOVIE")).check(matches(isDisplayed()))
        onView(withText("MOVIE")).perform(click())
        onView(withId(R.id.rvMovies)).check(matches(isDisplayed()))
        onView(withId(R.id.rvMovies)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                click()
            )
        )
        checkDetail(item, "movie")
    }

    @Test
    fun testDetailFilmFromSeriesTab() {
        val item = seriesApi[position]
        onView(withText("TV SHOW")).check(matches(isDisplayed()))
        onView(withText("TV SHOW")).perform(click())
        onView(withId(R.id.rvTvShow)).check(matches(isDisplayed()))
        onView(withId(R.id.rvTvShow)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                click()
            )
        )
        checkDetail(item, "tv")
    }

    private fun checkDetail(item: FilmModel, type: String? = null) {
        val toolbarTitle = if (type == "tv") "Tv Show" else "Film"
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar)).check(matches(withTitle("Detail $toolbarTitle")))

        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTitle)).check(matches(withText(item.title)))

        onView(withId(R.id.imgPoster)).check(matches(isDisplayed()))
        onView(withId(R.id.imgPoster)).check(matches(withTagValue(equalTo(item.poster))))

        onView(withId(R.id.tvReleaseDate)).check(matches(isDisplayed()))
        onView(withId(R.id.tvReleaseDate)).check(matches(withText(item.releaseDate)))

        onView(withId(R.id.chipGroup)).check(matches(isDisplayed()))
        for (element in item.genres) {
            onView(withText(element)).check(matches(isDisplayed()))
        }

        onView(withId(R.id.tvRating)).check(matches(isDisplayed()))
        onView(withId(R.id.tvRating)).check(matches(withText(item.rating.toString())))

        onView(withId(R.id.rating)).check(matches(isDisplayed()))
        onView(withId(R.id.rating)).check(matches(withRating(item.rating.roundRating() / 2)))

        onView(withId(R.id.tvSynopsis)).check(matches(isDisplayed()))
        onView(withId(R.id.tvSynopsis)).check(matches(withText(item.synopsis)))
    }
}
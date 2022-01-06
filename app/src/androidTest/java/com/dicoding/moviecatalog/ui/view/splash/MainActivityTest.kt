package com.dicoding.moviecatalog.ui.view.splash

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.data.model.DummyData
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.ui.view.main.MainActivity
import com.dicoding.moviecatalog.utils.CustomMatches.withRating
import com.dicoding.moviecatalog.utils.CustomMatches.withTitle
import com.dicoding.moviecatalog.utils.roundRating
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private val dummyData = DummyData()
    private val movies = dummyData.setMovies()
    private val series = dummyData.setSeries()
    private val random = dummyData.setRandom(12)
    private val position = 0

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMainView(){
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
                random.size
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
                movies.size
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
                series.size
            )
        )
    }

    @Test
    fun testDetailFilmFromRandom(){
        val item = random[position]
        onView(withId(R.id.rvRandom)).check(matches(isDisplayed()))
        onView(withId(R.id.rvRandom)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
        checkDetail(item)
    }

    @Test
    fun testDetailFilmFromMovieTab(){
        val item = movies[position]
        onView(withText("MOVIE")).check(matches(isDisplayed()))
        onView(withText("MOVIE")).perform(click())
        onView(withId(R.id.rvMovies)).check(matches(isDisplayed()))
        onView(withId(R.id.rvMovies)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
        checkDetail(item)
    }

    @Test
    fun testDetailFilmFromSeriesTab(){
        val item = series[position]
        onView(withText("TV SHOW")).check(matches(isDisplayed()))
        onView(withText("TV SHOW")).perform(click())
        onView(withId(R.id.rvTvShow)).check(matches(isDisplayed()))
        onView(withId(R.id.rvTvShow)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
        checkDetail(item)
    }

    private fun checkDetail(item: FilmModel){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar)).check(matches(withTitle("Detail ${item.type.type}")))

        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTitle)).check(matches(withText(item.title)))

        onView(withId(R.id.imgPoster)).check(matches(isDisplayed()))
        onView(withId(R.id.imgPoster)).check(matches(withTagValue(equalTo(item.poster))))

        onView(withId(R.id.tvReleaseDate)).check(matches(isDisplayed()))
        onView(withId(R.id.tvReleaseDate)).check(matches(withText(item.releaseDate)))

        onView(withId(R.id.chipGroup)).check(matches(isDisplayed()))
        for(element in item.genres){
            onView(withText(element)).check(matches(isDisplayed()))
        }

        onView(withId(R.id.tvRating)).check(matches(isDisplayed()))
        onView(withId(R.id.tvRating)).check(matches(withText(item.rating.toString())))

        onView(withId(R.id.rating)).check(matches(isDisplayed()))
        onView(withId(R.id.rating)).check(matches(withRating(item.rating.roundRating()/2)))

        onView(withId(R.id.tvSynopsis)).check(matches(isDisplayed()))
        onView(withId(R.id.tvSynopsis)).check(matches(withText(item.synopsis)))
    }
}
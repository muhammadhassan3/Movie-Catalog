package com.dicoding.moviecatalog.ui.view.splash

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dicoding.moviecatalog.BuildConfig
import com.dicoding.moviecatalog.R
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test

class SplashActivityTest{
    @get:Rule
    val scenario = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun testSplashScreen(){
        onView(withId(R.id.imgIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.imgIcon)).check(matches(withTagValue(equalTo(R.drawable.app_icon))))

        onView(withId(R.id.tvVersion)).check(matches(isDisplayed()))
        onView(withId(R.id.tvVersion)).check(matches(withText(BuildConfig.VERSION_NAME)))
    }
}
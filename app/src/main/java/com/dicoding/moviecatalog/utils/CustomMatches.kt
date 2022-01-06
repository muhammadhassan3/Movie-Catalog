package com.dicoding.moviecatalog.utils

import android.view.View
import android.widget.RatingBar
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object CustomMatches {
    fun withRating(rating: Float): Matcher<View> {
        return object: BoundedMatcher<View, RatingBar>(RatingBar::class.java){
            override fun describeTo(description: Description?) {
                description?.appendText("Checking the matcher on received view: ")
                description?.appendText("with expected star= $rating")
            }

            override fun matchesSafely(item: RatingBar?): Boolean {
                return item?.rating == rating
            }
        }
    }

    fun withTitle(title: String): Matcher<View> {
        return object: BoundedMatcher<View, Toolbar>(Toolbar::class.java){
            override fun describeTo(description: Description?) {
                description?.appendText("Checking the matcher on received view: ")
                description?.appendText("with expected title= $title")
            }

            override fun matchesSafely(item: Toolbar?): Boolean {
                return item?.title == title
            }
        }
    }
}
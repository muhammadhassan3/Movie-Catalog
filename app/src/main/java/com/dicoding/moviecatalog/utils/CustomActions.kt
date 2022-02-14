package com.dicoding.moviecatalog.utils

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf

object CustomActions {

    fun typeQuery(query: String) = object: ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
        }

        override fun getDescription(): String {
            return "Type $query to Search View"
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as SearchView).setQuery(query, true)
        }

    }
}
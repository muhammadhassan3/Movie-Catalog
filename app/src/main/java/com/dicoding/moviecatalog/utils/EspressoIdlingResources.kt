package com.dicoding.moviecatalog.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResources {
    private const val RESOURCE = "GLOBAl"
    val idlingResources = CountingIdlingResource(RESOURCE)

    fun increment(){
        idlingResources.increment()
    }

    fun decrement(){
        idlingResources.decrement()
    }
}
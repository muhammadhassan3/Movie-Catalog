package com.dicoding.moviecatalog.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.model.jsonmodel.MovieModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.RandomModelJson
import com.dicoding.moviecatalog.data.model.jsonmodel.SeriesModelJson
import com.google.android.material.snackbar.Snackbar

fun View.gone(){
    animate().alpha(0f)
        .setListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                visibility = View.GONE
            }
        }).duration = 200
}

fun View.visible(){
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f)
        .setListener(null).duration = 200
}

fun View.showSnackbar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun ImageView.loadImage(url: Any, tag: Any) {
    this.tag = tag
    Glide.with(this).load(url).placeholder(R.drawable.image_placeholder)
        .error(R.drawable.image_broken)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .centerCrop()
        .into(this)
}

fun Float.roundRating(): Float{
    if(this/10 > 1) throw IllegalArgumentException("Rating cannot greather than 10")
    val a = this * 10
    val b = a % 10
    return if(b <5 ){
        (a-b)/10
    }else{
        (a-b+10)/10
    }
}

fun provideRequestErrorMessage(errorCode: Int, errorMessage: String): String {
    return "Error code: $errorCode, Message: $errorMessage"
}

fun MovieModelJson.asDomain(): FilmModel {
    val genre = genres.map { it.name }
    return FilmModel(title, rating, releaseDate, genre, synopsis, type, poster, id)
}

fun SeriesModelJson.asDomain(): FilmModel {
    val genre = genres.map { it.name }
    return FilmModel(title, rating, releaseDate, genre, synopsis, type, poster, id)
}

fun RandomModelJson.asDomain(): FilmModel {
    val genre = genres.map { it.name }
    val title = title ?: originalName
    return FilmModel(title, rating, releaseDate, genre, synopsis, type, poster, id)
}
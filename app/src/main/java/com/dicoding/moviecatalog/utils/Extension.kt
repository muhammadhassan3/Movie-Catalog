package com.dicoding.moviecatalog.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.dicoding.moviecatalog.R

fun View.gone(){
    this.visibility = View.GONE
}

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun ImageView.loadImage(url: String){
    Glide.with(this).load(url).into(this)
        .clearOnDetach()
}
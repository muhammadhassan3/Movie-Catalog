package com.dicoding.moviecatalog.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dicoding.moviecatalog.R

fun ImageView.loadImage(url: Any, shouldRetry: Boolean, tag: Any) {
    this.tag = tag
    Glide.with(this).load(url).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            if (shouldRetry) {
                this@loadImage.loadImage(url, false, tag)
            } else Log.e("Glide", "Failed load image ${e.toString()}")
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

    }).placeholder(R.drawable.image_placeholder).error(R.drawable.image_broken).centerCrop()
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

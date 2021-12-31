package com.dicoding.moviecatalog.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dicoding.moviecatalog.R
import com.facebook.shimmer.ShimmerFrameLayout

fun View.gone(){
    this.visibility = View.GONE
}

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun ImageView.loadImage(url: String, shouldRetry: Boolean){
    Glide.with(this).load(url).listener(object: RequestListener<Drawable>{
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            if(shouldRetry){
                this@loadImage.loadImage(url, false)
            }else Log.e("Glide", "Failed load image ${e.toString()}")
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

    }).placeholder(R.drawable.image_placeholder).error(R.drawable.image_broken).centerCrop().into(this)
}

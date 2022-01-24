package com.dicoding.moviecatalog.ui.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.moviecatalog.BuildConfig
import com.dicoding.moviecatalog.R
import com.dicoding.moviecatalog.databinding.ActivitySplashBinding
import com.dicoding.moviecatalog.ui.view.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvVersion.text = BuildConfig.VERSION_NAME
        binding.imgIcon.tag = R.drawable.app_icon

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, delayTime)
    }

    companion object{
        private const val delayTime = 5000L
    }
}
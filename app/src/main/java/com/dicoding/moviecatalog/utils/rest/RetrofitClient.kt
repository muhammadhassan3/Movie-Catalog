package com.dicoding.moviecatalog.utils.rest

import com.dicoding.moviecatalog.BuildConfig
import com.dicoding.moviecatalog.utils.Constant
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


@ExperimentalSerializationApi
class RetrofitClient {
    companion object{
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private var INSTANCE: Retrofit? = null

        private fun getClient(): Retrofit{
            return if(INSTANCE != null) INSTANCE!! else{
                val jsonFactory = Constant.json.asConverterFactory("application/json".toMediaType())
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
                val client = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor {
                        val request = it.request().newBuilder()
                        request.addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                        it.proceed(request.build())
                    }
                if(BuildConfig.DEBUG){
                    client.addInterceptor(loggingInterceptor)
                }
                val instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .addConverterFactory(jsonFactory)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun getFilmApi(): FilmApiInterface{
            return getClient().create(FilmApiInterface::class.java)
        }
    }
}
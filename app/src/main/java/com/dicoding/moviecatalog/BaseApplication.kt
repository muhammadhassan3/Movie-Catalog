package com.dicoding.moviecatalog

import android.app.Application
import com.dicoding.moviecatalog.utils.Module.apiModule
import com.dicoding.moviecatalog.utils.Module.dataSourceModule
import com.dicoding.moviecatalog.utils.Module.databaseModule
import com.dicoding.moviecatalog.utils.Module.repositoryModule
import com.dicoding.moviecatalog.utils.Module.useCaseModule
import com.dicoding.moviecatalog.utils.Module.viewModelModule
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@ExperimentalSerializationApi
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BaseApplication)
            modules(listOf(
                viewModelModule,
                apiModule,
                repositoryModule,
                useCaseModule,
                dataSourceModule,
                databaseModule
            ))
        }
    }

}
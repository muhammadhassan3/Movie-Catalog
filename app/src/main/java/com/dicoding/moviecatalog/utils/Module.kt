package com.dicoding.moviecatalog.utils

import android.content.Context
import androidx.room.Room
import com.dicoding.moviecatalog.data.database.FilmDatabase
import com.dicoding.moviecatalog.data.datasource.LocalDataSource
import com.dicoding.moviecatalog.data.datasource.RemoteDataSource
import com.dicoding.moviecatalog.data.interactor.*
import com.dicoding.moviecatalog.data.repository.FavoriteRepository
import com.dicoding.moviecatalog.data.repository.FavoriteRepositoryImpl
import com.dicoding.moviecatalog.data.repository.FilmRepository
import com.dicoding.moviecatalog.data.repository.FilmRepositoryImpl
import com.dicoding.moviecatalog.data.usecase.*
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmViewModel
import com.dicoding.moviecatalog.ui.view.favorite.fragment.FavoriteMoviesViewModel
import com.dicoding.moviecatalog.ui.view.favorite.fragment.FavoriteSeriesViewModel
import com.dicoding.moviecatalog.ui.view.main.MainViewModel
import com.dicoding.moviecatalog.ui.view.search.SearchViewModel
import com.dicoding.moviecatalog.utils.rest.RetrofitClient
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Module {
    val viewModelModule = module {
        viewModel {
            DetailFilmViewModel(get())
        }
        viewModel {
            MainViewModel(get())
        }
        viewModel {
            FavoriteMoviesViewModel(get())
        }
        viewModel {
            FavoriteSeriesViewModel(get())
        }
        viewModel {
            SearchViewModel(get())
        }
    }

    @ExperimentalSerializationApi
    val apiModule = module {
        single {
            RetrofitClient.getFilmApi()
        }
    }

    val databaseModule = module {
        fun provideDatabase(context: Context): FilmDatabase = Room.databaseBuilder(
            context,
            FilmDatabase::class.java,
            "film_db"
        ).fallbackToDestructiveMigration()
            .build()
        single {
            provideDatabase(androidContext())
        }
    }

    val dataSourceModule = module {
        single { RemoteDataSource.getInstance(get()) }
        single { LocalDataSource.getInstance(get()) }
    }

    val repositoryModule = module {
        single<FilmRepository> { FilmRepositoryImpl.getInstance(get()) }
        single<FavoriteRepository> { FavoriteRepositoryImpl.getInstance(get()) }
    }

    val useCaseModule = module {
        factory<FilmDetailUseCase> { FilmDetailInteractor(get(), get()) }
        factory<FilmListUseCase> { FilmListInteractor(get()) }
        factory<MovieFavoriteUseCase> { MovieFavoriteInteractor(get()) }
        factory<SeriesFavoriteUseCase> { SeriesFavoriteInteractor(get()) }
        factory<SearchUseCase>{ SearchInteractor(get()) }
    }
}
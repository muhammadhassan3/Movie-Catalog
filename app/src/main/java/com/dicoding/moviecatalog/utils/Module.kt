package com.dicoding.moviecatalog.utils

import com.dicoding.moviecatalog.data.interactor.FilmDetailInteractor
import com.dicoding.moviecatalog.data.interactor.FilmListInteractor
import com.dicoding.moviecatalog.data.repository.FilmRepository
import com.dicoding.moviecatalog.data.remote.RemoteDataSource
import com.dicoding.moviecatalog.data.repository.FilmRepositoryImpl
import com.dicoding.moviecatalog.data.usecase.FilmDetailUseCase
import com.dicoding.moviecatalog.data.usecase.FilmListUseCase
import com.dicoding.moviecatalog.ui.view.detail.DetailFilmViewModel
import com.dicoding.moviecatalog.ui.view.main.MainViewModel
import com.dicoding.moviecatalog.utils.rest.RetrofitClient
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Module {
    val viewModelModule = module {
        viewModel{
            DetailFilmViewModel(get())
        }
        viewModel{
            MainViewModel(get())
        }
    }

    @ExperimentalSerializationApi
    val apiModule = module{
        single {
            RetrofitClient.getFilmApi()
        }
    }

    val dataSourceModule = module {
        single { RemoteDataSource.getInstance(get()) }
    }

    val repositoryModule = module {
        single<FilmRepository> { FilmRepositoryImpl.getInstance(get()) }
    }

    val useCaseModule = module {
        factory<FilmDetailUseCase> { FilmDetailInteractor(get()) }
        factory<FilmListUseCase> { FilmListInteractor(get()) }
    }
}
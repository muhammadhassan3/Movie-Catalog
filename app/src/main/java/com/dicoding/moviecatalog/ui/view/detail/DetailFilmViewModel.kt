package com.dicoding.moviecatalog.ui.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.data.usecase.FilmDetailUseCase
import com.dicoding.moviecatalog.utils.ApiResponse

class DetailFilmViewModel(private val useCase: FilmDetailUseCase) : ViewModel() {
    lateinit var data: LiveData<ApiResponse<FilmModel>>
    lateinit var favoriteData: LiveData<FavoriteEntity?>

    fun setData(id: Int, type: String) {
        data = if (type == "tv") {
            useCase.getDetailSeries(id)
        } else {
            useCase.getDetailMovie(id)
        }
    }

    fun getFavorite(id: Long){
        favoriteData = useCase.getFavoriteById(id)
    }

    fun removeFavorite(id: Long) = useCase.deleteFavoriteById(id)

    fun insertFavorite(favorite: FavoriteEntity) = useCase.insertFavorite(favorite)
}
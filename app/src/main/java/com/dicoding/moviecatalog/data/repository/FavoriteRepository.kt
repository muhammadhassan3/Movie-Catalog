package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity

interface FavoriteRepository {
    fun insert(favorite: FavoriteEntity)
    fun getFavoriteData(type: String): LiveData<PagingData<FavoriteEntity>>
    fun getFavoriteDataCount(type: String): LiveData<Int>
    fun getFavoriteById(id: Long): LiveData<FavoriteEntity?>
    fun deleteFavoriteById(id: Long)
}
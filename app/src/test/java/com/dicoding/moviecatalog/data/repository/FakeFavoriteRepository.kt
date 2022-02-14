package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.datasource.LocalDataSource

class FakeFavoriteRepository(private val dataSource: LocalDataSource): FavoriteRepository {
    override fun insert(favorite: FavoriteEntity) {
        dataSource.insert(favorite)
    }

    override fun getFavoriteData(type: String): LiveData<PagingData<FavoriteEntity>> = dataSource.getFavoriteData(type)

    override fun getFavoriteDataCount(type: String): LiveData<Int> = dataSource.getFavoriteDataSize(type)

    override fun getFavoriteById(id: Long): LiveData<FavoriteEntity?> = dataSource.getFavoriteById(id)

    override fun deleteFavoriteById(id: Long) = dataSource.deleteFavoriteById(id)
}
package com.dicoding.moviecatalog.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.data.datasource.LocalDataSource

class FavoriteRepositoryImpl(private val dataSource: LocalDataSource) : FavoriteRepository {

    override fun insert(favorite: FavoriteEntity) {
        dataSource.insert(favorite)
    }

    override fun getFavoriteData(
        type: String
    ): LiveData<PagingData<FavoriteEntity>> = dataSource.getFavoriteData(type)

    override fun getFavoriteDataCount(type: String): LiveData<Int> = dataSource.getFavoriteDataSize(type)

    override fun getFavoriteById(id: Long): LiveData<FavoriteEntity?> =
        dataSource.getFavoriteById(id)

    override fun deleteFavoriteById(id: Long) = dataSource.deleteFavoriteById(id)

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepositoryImpl? = null

        private var initialLoadKey: Int? = null

        fun getInstance(dataSource: LocalDataSource): FavoriteRepositoryImpl =
            INSTANCE ?: synchronized(this) {
                val instance = FavoriteRepositoryImpl(dataSource)
                INSTANCE = instance
                instance
            }
    }
}
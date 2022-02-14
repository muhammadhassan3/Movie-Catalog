package com.dicoding.moviecatalog.data.datasource

import androidx.lifecycle.LiveData
import androidx.paging.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.dicoding.moviecatalog.data.database.FilmDatabase
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity
import com.dicoding.moviecatalog.utils.Constant.NETWORK_PAGE_SIZE
import com.dicoding.moviecatalog.utils.QueryCollection.ASCENDING
import com.dicoding.moviecatalog.utils.QueryCollection.MOVIE
import com.dicoding.moviecatalog.utils.QueryCollection.TV_SHOW
import java.util.concurrent.Executors

class LocalDataSource private constructor(db: FilmDatabase) {
    private val favDao = db.favoriteDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getFavoriteData(type: String): LiveData<PagingData<FavoriteEntity>> = Pager(
        config = PagingConfig(
            NETWORK_PAGE_SIZE
        )
    ) {
        favDao.getFavoriteByMediaType(type)
    }.liveData

    fun getFavoriteDataSize(type: String): LiveData<Int> = favDao.getDataCountByMediaType(type)

    fun insert(favorite: FavoriteEntity) = executor.execute {
        favDao.insert(favorite)
    }

    fun getFavoriteById(id: Long) = favDao.getFavoriteById(id)

    fun deleteFavoriteById(id: Long) = executor.execute {
        favDao.deleteFavoriteById(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(db: FilmDatabase): LocalDataSource = INSTANCE ?: synchronized(this) {
            val instance = LocalDataSource(db)
            INSTANCE = instance
            instance
        }
    }

}
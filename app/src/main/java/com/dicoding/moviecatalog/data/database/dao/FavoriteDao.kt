package com.dicoding.moviecatalog.data.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: FavoriteEntity)

    @Query("select * from FavoriteEntity where item_id = :id")
    fun getFavoriteById(id: Long): LiveData<FavoriteEntity?>

    @Query("select * from FavoriteEntity where media_type = :type")
    fun getFavoriteByMediaType(type: String): PagingSource<Int, FavoriteEntity>

    @Query("delete from FavoriteEntity where item_id = :id")
    fun deleteFavoriteById(id: Long)

    @Query("select count(*) from FavoriteEntity where media_type=:type")
    fun getDataCountByMediaType(type: String): LiveData<Int>
}
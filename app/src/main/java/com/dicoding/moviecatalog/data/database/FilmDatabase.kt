package com.dicoding.moviecatalog.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.moviecatalog.data.database.dao.FavoriteDao
import com.dicoding.moviecatalog.data.database.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class FilmDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
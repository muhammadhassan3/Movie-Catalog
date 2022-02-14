package com.dicoding.moviecatalog.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object QueryCollection {
    const val MOVIE = "movie"
    const val TV_SHOW = "tv"
    const val ASCENDING = "asc"
    const val DESCENDING = "desc"
    const val ID = "id"
    const val RELEASE_DATE = "release_date"
    const val ITEM_ID = "item_id"
    const val TITLE =  "title"

    fun getFavoriteByMediaTypeAndFilter(type: String, filter: String, sorting: String): SimpleSQLiteQuery{
        val baseQuery = StringBuilder().append("select * from FavoriteEntity ")
        val query = baseQuery.append("where media_type=$type order by $filter $sorting")
        return SimpleSQLiteQuery(query.toString())
    }

    fun getFavoriteDataSizeByMediaTypeAndFilter(type: String, filter: String, sorting: String): SimpleSQLiteQuery{
        val baseQuery = StringBuilder().append("select count(id) from FavoriteEntity ")
        val query = baseQuery.append("where mediaType=\"movie\"")
        return SimpleSQLiteQuery(query.toString())
    }
}
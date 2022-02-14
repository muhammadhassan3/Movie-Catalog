package com.dicoding.moviecatalog.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.moviecatalog.data.datasource.RemoteDataSource
import com.dicoding.moviecatalog.data.model.FilmModel
import com.dicoding.moviecatalog.utils.Constant.NETWORK_PAGE_SIZE
import com.dicoding.moviecatalog.utils.Constant.TMDB_STARTING_PAGE
import com.dicoding.moviecatalog.utils.Status
import com.dicoding.moviecatalog.utils.exception.NoDataAvailableException
import okio.IOException
import retrofit2.HttpException
import java.lang.RuntimeException

class SearchPagingSource(private val dataSource: RemoteDataSource, private val query: String): PagingSource<Int, FilmModel>() {
    override fun getRefreshKey(state: PagingState<Int, FilmModel>): Int? {
        return state.anchorPosition?.let{anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmModel> {
        val pageIndex = params.key ?: TMDB_STARTING_PAGE
        return try{
            val response = dataSource.getSearchResult(pageIndex, query)
            when (response.status) {
                Status.SUCCESS -> {
                    if (response.data != null) {
                        val nextKey = if (response.data.isEmpty() || pageIndex==500) {
                            null
                        } else {
                            pageIndex + (response.data.size / NETWORK_PAGE_SIZE)
                        }
                        LoadResult.Page(
                            data = response.data,
                            prevKey = if (pageIndex == TMDB_STARTING_PAGE) null else pageIndex,
                            nextKey = nextKey
                        )
                    } else LoadResult.Invalid()
                }
                Status.ERROR -> LoadResult.Error(IllegalStateException(response.message?.getContentIfNotHandled()))
                Status.NO_DATA -> LoadResult.Error(NoDataAvailableException())
                else -> LoadResult.Invalid()
            }
        }catch (ioException: IOException){
            LoadResult.Error(ioException)
        }catch (runtimeException: RuntimeException){
            LoadResult.Error(runtimeException)
        }
    }
}
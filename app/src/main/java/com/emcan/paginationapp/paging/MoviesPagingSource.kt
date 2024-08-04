package com.emcan.paginationapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emcan.paginationapp.network.ApiService
import com.emcan.paginationapp.pojos.MoviesListResponse
import retrofit2.HttpException

class MoviesPagingSource(
    private val apiService: ApiService ,
) : PagingSource<Int, MoviesListResponse.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesListResponse.Result> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getMoviesList(currentPage)
            Log.e("TAG", "load: "+response )
            val data = response.body()!!.results
            val responseData = mutableListOf<MoviesListResponse.Result>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, MoviesListResponse.Result>): Int? {
        return null
    }


}
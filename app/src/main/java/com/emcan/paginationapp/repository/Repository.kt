package com.emcan.paginationapp.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.emcan.paginationapp.network.ApiService
import com.emcan.paginationapp.network.RetrofitHelper
import com.emcan.paginationapp.paging.MoviesPagingSource
import com.emcan.paginationapp.pojos.MoviesListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class Repository private constructor() {
    val apiService: ApiService by lazy {
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }

    companion object {
        private var instance: Repository? = null
        fun getInstance(): Repository {
            return instance ?: synchronized(this) {
                val temp = Repository()
                instance = temp
                temp
            }
        }
    }

    fun getMoviesList(): Flow<PagingData<MoviesListResponse.Result>> {
        Log.e("TAG", "repository: " )
        return Pager(config = PagingConfig(
            pageSize = 1,
            prefetchDistance = 2, initialLoadSize = 1
        ),
            pagingSourceFactory = {
                MoviesPagingSource(apiService)
            }).flow.map { it }

    }
}
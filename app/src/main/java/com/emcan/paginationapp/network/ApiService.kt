package com.emcan.paginationapp.network

import com.emcan.paginationapp.pojos.MoviesListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getMoviesList(@Query("page") page: Int): Response<MoviesListResponse>

}
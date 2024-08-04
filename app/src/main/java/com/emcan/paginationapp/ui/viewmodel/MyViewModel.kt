package com.emcan.paginationapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.emcan.paginationapp.pojos.MoviesListResponse
import com.emcan.paginationapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyViewModel(private val repository: Repository):ViewModel(){
    private val _movies: MutableStateFlow<PagingData<MoviesListResponse.Result>> =
        MutableStateFlow(PagingData.empty())
    val movies: StateFlow<PagingData<MoviesListResponse.Result>> = _movies

    fun getMoviesList() = viewModelScope.launch {
         repository.getMoviesList( )
            .cachedIn(viewModelScope)
            .collectLatest { pagingData ->
                Log.e("TAG", "viewmodel: "  )

                _movies .value = pagingData
            }

    }
}
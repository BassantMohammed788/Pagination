package com.emcan.paginationapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.emcan.paginationapp.databinding.FragmentMoviesBinding
import com.emcan.paginationapp.repository.Repository
import com.emcan.paginationapp.ui.adapter.LoadMoreAdapter
import com.emcan.paginationapp.ui.adapter.MoviesPagingAdapter
import com.emcan.paginationapp.ui.viewmodel.MyViewModel
import com.emcan.paginationapp.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MyViewModel
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var moviesPagingAdapter: MoviesPagingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelFactory = ViewModelFactory(
            Repository.getInstance()
        )
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MyViewModel::class.java)
        setUpProductAdapter()
        Log.e("TAG", "onViewCreated: " )
        viewModel.getMoviesList()
        observeGetProductsResponse()

    }

    private fun setUpProductAdapter() {
        moviesPagingAdapter = MoviesPagingAdapter(
            onProductClickListener = { product ->
            })
        binding.rlMovies.apply {
            adapter = moviesPagingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rlMovies.adapter = moviesPagingAdapter.withLoadStateFooter(
            LoadMoreAdapter {
                moviesPagingAdapter.retry()
            }
        )
    }
    private fun observeGetProductsResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movies.collect { result ->
                moviesPagingAdapter.submitData(result)
            }

        }
        lifecycleScope.launch {
            moviesPagingAdapter.loadStateFlow.collect {
                val state = it.refresh is LoadState.Loading
                if (state) {
                   binding.prgBarMovies.visibility=View.VISIBLE
                } else {
                    binding.prgBarMovies.visibility=View.GONE
                    binding.rlMovies.visibility=View.VISIBLE


                }

            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
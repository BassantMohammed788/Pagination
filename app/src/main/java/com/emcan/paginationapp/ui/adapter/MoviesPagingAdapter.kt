package com.emcan.paginationapp.ui.adapter


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.paging.log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.emcan.paginationapp.R
import com.emcan.paginationapp.databinding.ItemMoviesBinding
import com.emcan.paginationapp.pojos.MoviesListResponse
import com.emcan.paginationapp.utils.POSTER_BASE_URL

class MoviesPagingAdapter (private val onProductClickListener: (MoviesListResponse.Result) -> Unit) :
    PagingDataAdapter<MoviesListResponse.Result, MoviesPagingAdapter.ViewHolder>(OrderDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMoviesBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder(private val binding: ItemMoviesBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MoviesListResponse.Result) {
            binding.apply {
                 tvMovieName.text = item.title
                tvMovieDateRelease.text = item.releaseDate
                tvRate.text=item.voteAverage.toString()
                val moviePosterURL = POSTER_BASE_URL + item?.posterPath
                imgMovie.load(moviePosterURL){
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                tvLang.text=item.originalLanguage

                root.setOnClickListener {
                    onProductClickListener(item)
                }
            }
        }

    }

    class OrderDiffCallback : DiffUtil.ItemCallback<MoviesListResponse.Result>() {
        override fun areItemsTheSame(
            oldItem: MoviesListResponse.Result, newItem: MoviesListResponse.Result
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: MoviesListResponse.Result, newItem: MoviesListResponse.Result
        ): Boolean {
            return oldItem == newItem
        }
    }



}
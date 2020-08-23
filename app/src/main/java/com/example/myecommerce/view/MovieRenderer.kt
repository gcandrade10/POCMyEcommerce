package com.example.myecommerce.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myecommerce.Movie
import com.example.myecommerce.R
import com.example.myecommerce.databinding.MovieHolderBinding

private enum class Type { MOVIE }

private val type: Array<Type> = Type.values()

internal class MovieRenderer(private val moviesView: RecyclerView, private val listener: RendererListener) {

    private var movies: List<Pair<Movie, Int?>>? = null

    init {
        moviesView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MoviesAdapter()
        }
    }

    fun render(movies: List<Pair<Movie, Int?>>) {
        this.movies = movies
        moviesView.adapter?.notifyDataSetChanged()
    }

    private inner class MoviesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (type[viewType]) {
            Type.MOVIE -> MovieHolder(MovieHolderBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.movie_holder, parent, false)))
        }

        override fun getItemCount(): Int = movies?.size ?: 0

        override fun getItemId(position: Int): Long = position.toLong()

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (type[holder.itemViewType]) {
                Type.MOVIE -> movies?.run { (holder as MovieHolder).render(this[position]) }
            }
        }

        override fun getItemViewType(position: Int) = Type.MOVIE.ordinal
    }

    private inner class MovieHolder(private val binding: MovieHolderBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var movie: Pair<Movie, Int?>

        init {
            itemView.setOnClickListener { listener.onMovie(movie.first) }
        }

        fun render(movie: Pair<Movie, Int?>) {
            this.movie = movie
            binding.movieTitle.text = movie.first.title
            binding.include.addToCart.setOnClickListener {
                listener.onAddToCard(movie.first)
            }
            movie.second.run {
                binding.include.addToCart.isVisible = this == null || this == 0
                binding.include.main.isVisible = this != null && this > 0
                binding.include.quantity.text = this.toString()
            }
            binding.include.increase.setOnClickListener { listener.onAddToCard(movie.first) }
            binding.include.decrease.setOnClickListener { listener.decrease(movie.first) }
        }
    }

    interface RendererListener {
        fun onMovie(movie: Movie)
        fun onAddToCard(movie: Movie)
        fun decrease(movie: Movie)
    }
}
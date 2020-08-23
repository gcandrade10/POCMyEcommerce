package com.example.myecommerce.list

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myecommerce.Movie
import com.example.myecommerce.R
import com.example.myecommerce.view.MovieRenderer
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MovieListFragment : Fragment() {

    private lateinit var renderer: MovieRenderer
    private val viewModel by sharedViewModel<MovieListViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderer = MovieRenderer(recycler, object : MovieRenderer.RendererListener {
            override fun onMovie(movie: Movie) = findNavController().navigate(R.id.action_movieListFragment_to_detailFragment2, bundleOf("id" to movie.id))
            override fun onAddToCard(movie: Movie) = viewModel.increase(movie)
            override fun decrease(movie: Movie) = viewModel.decrease(movie)
        })
        viewModel.moviesLiveData.observe(viewLifecycleOwner, { movies -> renderer.render(movies) })
        viewModel.retrieveMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.shopping_cart) findNavController().navigate(R.id.action_movieListFragment_to_shoppingCartFragment)
        return super.onOptionsItemSelected(item)
    }
}
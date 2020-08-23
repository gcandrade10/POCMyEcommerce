package com.example.myecommerce.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myecommerce.Movie
import com.example.myecommerce.R
import com.example.myecommerce.view.MovieRenderer
import kotlinx.android.synthetic.main.fragment_shopping_cart.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ShoppingCartFragment : Fragment() {

    private lateinit var renderer: MovieRenderer
    private val viewModel by viewModel<ShoppingCartViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearShoppingCart.setOnClickListener { viewModel.clearCart() }
        renderer = MovieRenderer(cartRecycler, object : MovieRenderer.RendererListener {
            override fun onMovie(movie: Movie) = findNavController().navigate(R.id.action_shoppingCartFragment_to_detailFragment, bundleOf("id" to movie.id))
            override fun onAddToCard(movie: Movie) {
                viewModel.increase(movie)
            }

            override fun decrease(movie: Movie) = viewModel.decrease(movie)
        })
        viewModel.cartLiveData.observe(viewLifecycleOwner, { movies -> renderer.render(movies) })
        viewModel.retrieveCart()
    }

}
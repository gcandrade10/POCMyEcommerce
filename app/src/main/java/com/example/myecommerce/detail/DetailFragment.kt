package com.example.myecommerce.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.myecommerce.Movie
import com.example.myecommerce.R
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.increment_decrement.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {

    var movie: Movie? = null
    val args: DetailFragmentArgs by navArgs()
    private val detailViewModel by viewModel<DetailViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel.movieLiveData.observe(viewLifecycleOwner, {
            movie = it.first
            movieName.text = it.first.title
            movieOverview.text = it.first.overview
            it.second.run {
                addToCart.isVisible = this == null || this == 0
                main.isVisible = this != null && this > 0
                quantity.text = this.toString()
            }
        })
        detailViewModel.retrieveMovie(args.id)
        increase.setOnClickListener { movie?.let { detailViewModel.increase(it) } }
        decrease.setOnClickListener { movie?.let { detailViewModel.decrease(it) } }
        addToCart.setOnClickListener { movie?.let { detailViewModel.increase(it) } }
    }

}
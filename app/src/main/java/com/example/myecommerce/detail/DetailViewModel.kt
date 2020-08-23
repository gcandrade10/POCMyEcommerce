package com.example.myecommerce.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myecommerce.Movie
import com.example.myecommerce.persistence.Repository
import com.example.myecommerce.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class DetailViewModel(private val repository: Repository) : ViewModel() {

    val movieLiveData: MutableLiveData<Pair<Movie, Int?>> = MutableLiveData()

    private val disposable = CompositeDisposable()

    fun retrieveMovie(id: Long) = repository.getCartItem(id).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe { response -> movieLiveData.value = response }.addTo(disposable)


    fun increase(movie: Movie) = repository.addToCart(movieId = movie.id).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe {
            retrieveMovie(movie.id)
        }.addTo(disposable)


    fun decrease(movie: Movie) = repository.removeFromCart(movieId = movie.id).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe {
            retrieveMovie(movie.id)
        }.addTo(disposable)


    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}
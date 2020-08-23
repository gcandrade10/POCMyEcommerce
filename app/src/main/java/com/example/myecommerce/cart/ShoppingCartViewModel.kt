package com.example.myecommerce.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myecommerce.Movie
import com.example.myecommerce.persistence.Repository
import com.example.myecommerce.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class ShoppingCartViewModel(private val repository: Repository) : ViewModel() {

    val cartLiveData: MutableLiveData<List<Pair<Movie, Int?>>> = MutableLiveData()

    private val disposable = CompositeDisposable()

    fun increase(movie: Movie) = repository.addToCart(movieId = movie.id).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe {
            retrieveCart()
        }.addTo(disposable)


    fun decrease(movie: Movie) = repository.removeFromCart(movieId = movie.id).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe {
            retrieveCart()
        }.addTo(disposable)


    fun clearCart() = repository.clearCart().observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe {
            retrieveCart()
        }.addTo(disposable)

    fun retrieveCart() = repository.getCart().observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe {
            cartLiveData.value = it
        }.addTo(disposable)

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}
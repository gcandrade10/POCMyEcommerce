package com.example.myecommerce.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myecommerce.Movie
import com.example.myecommerce.persistence.Repository
import com.example.myecommerce.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MovieListViewModel(private val repository: Repository) : ViewModel() {

    val moviesLiveData: MutableLiveData<List<Pair<Movie, Int?>>> = MutableLiveData()

    private val disposable = CompositeDisposable()

    fun retrieveMovies() = repository.getMovies()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe { moviesLiveData.value = it }.addTo(disposable)


    fun increase(movie: Movie): Unit = repository.addToCart(movieId = movie.id).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe().addTo(disposable)


    fun decrease(movie: Movie) = repository.removeFromCart(movieId = movie.id).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe().addTo(disposable)


    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}
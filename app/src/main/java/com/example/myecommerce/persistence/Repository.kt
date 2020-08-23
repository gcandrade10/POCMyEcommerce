package com.example.myecommerce.persistence

import com.example.myecommerce.Movie
import com.example.myecommerce.MovieId
import com.example.myecommerce.remote.TmdbEndpoints
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

interface Repository {
    fun getCart(): Observable<List<Pair<Movie, Int>>>
    fun getCartItem(movieId: MovieId): Observable<Pair<Movie, Int>>
    fun getMovies(): Observable<List<Pair<Movie, Int>>>
    fun addToCart(movieId: MovieId): Completable
    fun removeFromCart(movieId: MovieId): Completable
    fun clearCart(): Completable
}


class RepositoryImpl(val remote: TmdbEndpoints, val database: AppDatabase) : Repository {

    val key = "cfacbd1b17a84295a04a55d573daa740"

    override fun getCart(): Observable<List<Pair<Movie, Int>>> = database.movieDao.getCart().map { list -> list.map { it.toMoviePair() } }

    override fun getCartItem(movieId: MovieId): Observable<Pair<Movie, Int>> = database.movieDao.getCartItem(movieId).map { it.toMoviePair() }

    override fun getMovies(): Observable<List<Pair<Movie, Int>>> {
        val disposable = remote.getMovies(key).map { it.results.map { movie -> movie to 0 } }.subscribeOn(Schedulers.io()).subscribe { list ->
            database.movieDao.add(list.map { it.toMovieEntity() })
                .subscribeOn(Schedulers.io()).subscribe()
        }
        return database.movieDao.findAll().map { list -> list.map { it.toMoviePair() } }
    }

    override fun addToCart(movieId: MovieId) = database.movieDao.increase(movieId)

    override fun removeFromCart(movieId: MovieId) = database.movieDao.decrease(movieId)

    override fun clearCart() = database.movieDao.clearCart()
}

fun Pair<Movie, Int>.toMovieEntity() = MovieEntity(first.id, first.title, first.overview, second)
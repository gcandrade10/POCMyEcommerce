package com.example.myecommerce.remote

import com.example.myecommerce.Movie
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object ServiceBuilder {
    private val client = OkHttpClient
        .Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(TmdbEndpoints::class.java)

    fun buildService(): TmdbEndpoints {
        return retrofit
    }
}

interface TmdbEndpoints {

    @GET("/3/discover/movie")
    fun getMovies(@Query("api_key") key: String): Observable<Movies>

    @GET("/3/movie/{id}")
    fun getMovie(@Path("id") id: Long, @Query("api_key") key: String):Observable<Movie>

}

class Movies(val results: List<Movie>)
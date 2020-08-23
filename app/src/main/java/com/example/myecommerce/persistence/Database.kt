package com.example.myecommerce.persistence

import android.content.Context
import androidx.room.*
import com.example.myecommerce.Movie
import com.example.myecommerce.MovieId
import io.reactivex.Completable
import io.reactivex.Observable

@Entity(tableName = "movies")
data class MovieEntity(@PrimaryKey val id: Long, val title: String, val overview: String, val quantity: Int)

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun findAll(): Observable<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(users: List<MovieEntity>): Completable

    @Query("SELECT * FROM movies WHERE quantity != 0")
    fun getCart(): Observable<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id == :id")
    fun getCartItem(id: MovieId): Observable<MovieEntity>

    @Query("UPDATE movies SET quantity = quantity + 1 WHERE ID = :id")
    fun increase(id: MovieId): Completable

    @Query("UPDATE movies SET quantity = quantity - 1 WHERE ID = :id")
    fun decrease(id: MovieId): Completable

    @Query("UPDATE movies SET quantity = 0")
    fun clearCart(): Completable
}

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object {
        fun getAppDatabase(context: Context): AppDatabase {
            synchronized(AppDatabase::class) {
                return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "moviesDB").build()
            }
        }
    }
}

fun MovieEntity.toMoviePair() = Movie(id, title, overview) to quantity
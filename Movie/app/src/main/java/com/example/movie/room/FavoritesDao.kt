package com.example.movie.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favmovies_table ")
    fun getAllFavorites(): LiveData<List<Favorites>>

    @Insert
    suspend fun insertAll(favorites: Favorites)

    @Delete
    suspend fun deleteFavorite(favorites: Favorites)

    @Query("SELECT * FROM favMovies_table WHERE id = :movieId")
    suspend fun getFavoriteById(movieId: Int): Favorites?

    @Query("SELECT COUNT(*) FROM favMovies_table WHERE id = :movieId")
    fun isMovieInFavorites(movieId: Int): Int
}
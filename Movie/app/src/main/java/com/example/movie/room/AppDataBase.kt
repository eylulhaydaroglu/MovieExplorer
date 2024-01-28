package com.example.movie.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favorites::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao


}
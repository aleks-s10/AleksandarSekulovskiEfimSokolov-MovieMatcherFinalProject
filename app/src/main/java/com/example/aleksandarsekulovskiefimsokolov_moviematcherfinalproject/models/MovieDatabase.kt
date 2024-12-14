package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieDB::class, UserDB::class, GroupDB::class, SessionDB::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

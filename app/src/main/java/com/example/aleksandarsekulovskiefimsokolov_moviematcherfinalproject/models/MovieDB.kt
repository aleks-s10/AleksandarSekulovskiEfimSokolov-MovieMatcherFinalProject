package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "movies")
data class MovieDB(
    @PrimaryKey val id: String,
    val description: String,
    val genre: String,
    val poster: String,
    val rating: Double,
    val release_year: String,
    val title: String,
    val page: Int,
    val favorite: Boolean,
)

@Entity(tableName = "users")
@TypeConverters(Converters::class)
data class UserDB(
    @PrimaryKey val userID: String,
    val userName: String,
    val profilePicture: Int,
    val movies: List<String>,
    val sessions: List<String>,
    val email: String,
    val firstName: String,
    val lastName: String,
    val favoriteGenre: String,
    val pending: Boolean,
    val self: Int,
)

@Entity(tableName = "groups")
@TypeConverters(Converters::class)
data class GroupDB (
    @PrimaryKey val groupID: String,
    val users: Map<String, List<String>>,
    val movies: Map<String,List<Int>>,
    val pending: Boolean,
    val numUsers: Int = 0,
    val finalMovie: String = "",
    val sessionName: String = "",
)

@Entity(tableName = "sessions")
@TypeConverters(Converters::class)
data class SessionDB (
    @PrimaryKey val sessionID: String,
    val movies: Map<String, Int>,
    val users: Map<String, List<String>>,
    val numUsers: Int
)



class Converters {

    @TypeConverter
    fun listToString(input: List<String>): String {
        return input.joinToString(separator = "@")
    }
    @TypeConverter
    fun stringToList(input: String): List<String> {
        return input.split("@")
    }

    @TypeConverter
    fun mapToString(input: Map<String, Int>): String {
        return Gson().toJson(input)
    }

    @TypeConverter
    fun stringToMap(input: String): Map<String, Int> {
        val type = object : TypeToken<Map<String, Int>>() {}.type
        return Gson().fromJson(input, type)
    }

    @TypeConverter
    fun mapListToString(input: Map<String, List<String>>): String {
        return Gson().toJson(input)
    }

    @TypeConverter
    fun mapListToInt(input: Map<String, List<Int>>): String {
        return Gson().toJson(input)
    }

    @TypeConverter
    fun intToMapList(input: String): Map<String, List<Int>> {
        val type = object : TypeToken<Map<String, List<Int>>>() {}.type
        return Gson().fromJson(input, type)
    }

    @TypeConverter
    fun stringToMapList(input: String): Map<String, List<String>> {
        val type = object : TypeToken<Map<String, List<String>>>() {}.type
        return Gson().fromJson(input, type)
    }


}
package com.example.movie.data

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MovieList(

    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val movieList: ArrayList<Movie>
)

data class Movie(
    @PrimaryKey(autoGenerate = true)

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    var isVisible: Boolean,

    @SerializedName("budget")
    val budget: Long,

    @SerializedName("genres")
    val genres: List<Genre>,


    @SerializedName("id")
    val id: Int,

    @SerializedName("imdb_id")
    val imdbId: String?,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("poster_path")
    val posterPath: String?,


    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("revenue")
    val revenue: Long,

    @SerializedName("runtime")
    val runtime: Int,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,

    @SerializedName("status")
    val status: String,

    @SerializedName("tagline")
    val tagline: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("video")
    val video: Boolean,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int

)


data class Genre(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)


data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String,

    @SerializedName("iso_639_1")
    val iso6391: String,

    @SerializedName("name")
    val name: String
)

data class SearchResults(
    @SerializedName("results") val movieList: ArrayList<Movie>
)
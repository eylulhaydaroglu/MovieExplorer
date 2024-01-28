package com.example.movie

import com.example.movie.data.Movie
import com.example.movie.data.MovieList
import com.example.movie.data.SearchResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular${BundleKey.api_key_url}")
    suspend fun getListInfo(@Query("page") page: Int): Response<MovieList>

    @GET("movie/{movie_id}${BundleKey.api_key_url}")
    suspend fun getDetailInfo(@Path("movie_id") id: String): Response<Movie>

    @GET("search/movie${BundleKey.api_key_url}")
    suspend fun searchMovies(@Query("query") query: String?): Response<SearchResults>
}
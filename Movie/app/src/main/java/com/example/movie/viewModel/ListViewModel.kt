package com.example.movie.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.data.MovieList
import com.example.movie.MovieService
import com.example.movie.data.Movie
import com.example.movie.room.FavoritesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val movieService: MovieService, private val favoritesDao: FavoritesDao
) : ViewModel() {
    private val _liveDataSearchResults = MutableLiveData<ArrayList<Movie>>()
    val liveDataSearchResults: LiveData<ArrayList<Movie>> = _liveDataSearchResults
    private val _liveDataMovieList = MutableLiveData<MovieList>()
    val liveDataMovieList: LiveData<MovieList> = _liveDataMovieList
    private var currentPage = 1
    var isScrollRequestInProgress = false

    init {
        loadMovies(currentPage)
    }

    fun loadMovies(page: Int) {
        viewModelScope.launch {
            try {
                isScrollRequestInProgress = true
                val response = movieService.getListInfo(page)
                if (response.isSuccessful) {
                    _liveDataMovieList.value = response.body()

                }
                isScrollRequestInProgress = false
            } catch (e: Exception) {
                isScrollRequestInProgress = false

            }
        }
    }

    fun loadNextPage() {
        if (!isScrollRequestInProgress) {
            currentPage++
            loadMovies(currentPage)
        }
    }

    fun loadPreviousPage() {
        if (!isScrollRequestInProgress && currentPage > 1) {
            currentPage--
            loadMovies(currentPage)
        }
    }

    fun display() {
        loadMovies(currentPage)
    }

    fun searchMovies(query: String?) {
        viewModelScope.launch {
            try {
                val response = movieService.searchMovies(query)
                if (response.isSuccessful) {
                    _liveDataSearchResults.value = response.body()?.movieList
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateMovieVisibility(movieList: List<Movie>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (movie in movieList) {
                movie.isVisible = favoritesDao.isMovieInFavorites(movie.id) > 0
            }
        }
    }
}




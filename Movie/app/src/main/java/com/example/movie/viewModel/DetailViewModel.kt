package com.example.movie.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.MovieService
import com.example.movie.data.Movie
import com.example.movie.room.Favorites
import com.example.movie.room.FavoritesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieService: MovieService,
    private val favoritesDao: FavoritesDao
) :
    ViewModel() {

    private val _liveDataMovieDetail = MutableLiveData<Movie?>()
    val liveDataMovieDetail: MutableLiveData<Movie?> = _liveDataMovieDetail
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage
    val liveDataLoading =
        MutableLiveData<Boolean>()//mutable olduğu zaman postvalue ile içerik değişebilir

    fun callMovieDetail(id: String) {
        liveDataLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response: Response<Movie> = movieService.getDetailInfo(id)
                Log.e("Call", response.toString())
                if (response.isSuccessful) {

                    val movie: Movie? = response.body()
                    _liveDataMovieDetail.postValue(movie)
                } else {
                    _liveDataMovieDetail.postValue(null)
                }

            } catch (exception: Exception) {
                Log.e(
                    "Find error", exception.message.orEmpty()
                )
                _liveDataMovieDetail.postValue(null)
            } finally {
                liveDataLoading.postValue(false)
            }
        }
    }

    private var movieId: Int = 0
    fun display(id: String) {

        try {
            movieId = id.toInt()
            Log.e("movieId", movieId.toString())
            viewModelScope.launch {
                getFavoriteStatus()
                callMovieDetail(id)
            }
        } catch (e: NumberFormatException) {
            Log.e("display", "Invalid id format: $id")
        }

    }

    fun toggleFavorite(movie_num: Int) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isFavorite = favoritesDao.getFavoriteById(movie_num) != null
                if (isFavorite) {
                    favoritesDao.getFavoriteById(movie_num)?.let { favoritesDao.deleteFavorite(it) }
                    _isFavorite.postValue(false)
                    _toastMessage.postValue("Deleted from Favorites")
                } else {
                    val newFavorites = Favorites(movie_num, isFavorite)
                    favoritesDao.insertAll(newFavorites)
                    _isFavorite.postValue(true)
                    _toastMessage.postValue("Added to Favorites")
                }

            }
        }
    }


    private suspend fun getFavoriteStatus() {
        Log.e("getstatuse girildi", movieId.toString())
        val isFavorite = favoritesDao.getFavoriteById(movieId) != null
        _isFavorite.postValue(isFavorite)
    }


}







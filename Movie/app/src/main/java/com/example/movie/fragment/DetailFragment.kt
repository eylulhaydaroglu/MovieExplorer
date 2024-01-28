package com.example.movie.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.movie.BundleKey
import com.example.movie.MovieService
import com.example.movie.R
import com.example.movie.data.Genre
import com.example.movie.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    var textViewname: TextView? = null
    var textViewsubtitle: TextView? = null
    var textViewimdb: TextView? = null
    var textViewyear: TextView? = null
    var textViewlanguage: TextView? = null
    var textViewlength: TextView? = null
    var textViewoverview: TextView? = null
    var textviewgenres: TextView? = null
    private var movieId: Int? = 0
    private var moviesubtitle: String? = ""
    private var movieImdb: String? = ""
    private var movieYear: String? = ""
    private var movieLanguage: String? = ""
    private var movieLength: String? = ""
    private var movieOverview: String? = ""
    private var movieName: String? = ""
    private var moviegenres: String? = ""
    private lateinit var movieBack: ImageView
    private lateinit var movieFront: ImageView
    private lateinit var imageViewFavorite: ImageView

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val movieurlid = BundleKey.movie_url_Id
        movieId =
            requireArguments().getString(movieurlid)?.toInt() ?: Log.e("avatar", movieId.toString())
        viewModel.display((movieId).toString())
        viewModel.toastMessage.observe(viewLifecycleOwner, { message ->
            message?.let {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        textViewname = view.findViewById(R.id.text_name)
        textViewsubtitle = view.findViewById(R.id.text_subtitle)
        textViewimdb = view.findViewById(R.id.text_imdb)
        textViewyear = view.findViewById(R.id.text_year)
        textViewlanguage = view.findViewById(R.id.text_language)
        textViewlength = view.findViewById(R.id.text_length)
        textViewoverview = view.findViewById(R.id.text_overview)
        movieBack = view.findViewById(R.id.imgMovieBack)
        movieFront = view.findViewById(R.id.imgMovie)
        imageViewFavorite = view.findViewById(R.id.imageViewFavorite)
        textviewgenres = view.findViewById(R.id.text_genders)
        imageViewFavorite.setOnClickListener {

            onFavoriteClicked(movieId!!)
        }
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                imageViewFavorite.setImageResource(R.drawable.ic_heart_filled)

            } else {
                imageViewFavorite.setImageResource(R.drawable.ic_heart_empty)

            }
        }


        observeViewModel()

        return view


    }

    private fun observeViewModel() {

        viewModel.liveDataMovieDetail.observe(viewLifecycleOwner) { movie ->

            movie?.let {


                movieName = it.originalTitle
                moviesubtitle = it.tagline
                movieImdb = it.voteAverage.toString() + " (" + it.voteCount.toString() + " )"
                movieYear = "Date:\n" + it.releaseDate
                movieLanguage = "Language:\n" + it.originalLanguage
                movieLength = "Length:\n" + it.runtime.toString() + "min"
                movieOverview = it.overview
                moviegenres = getGenreNames(it.genres)
                Glide.with(movieFront).load("https://image.tmdb.org/t/p/original" + it.posterPath)
                    .into(movieFront)
                Glide.with(movieBack).load("https://image.tmdb.org/t/p/original" + it.posterPath)
                    .into(movieBack)

            }


            textViewname?.text = movieName.toString()
            textViewsubtitle?.text = moviesubtitle.toString()
            textViewimdb?.text = movieImdb.toString()
            textViewyear?.text = movieYear.toString()
            textViewlanguage?.text = movieLanguage.toString()
            textViewlength?.text = movieLength.toString()
            textViewoverview?.text = movieOverview.toString()
            textviewgenres?.text = moviegenres.toString()


        }

    }

    private fun onFavoriteClicked(movieId: Int) {
        Log.e("Onfavoritechecked girildi", movieId.toString())
        viewModel.toggleFavorite(movieId)
    }

    private fun getGenreNames(genres: List<Genre>): String {
        val genreNames = genres.joinToString { it.name }
        return "$genreNames"
    }


}
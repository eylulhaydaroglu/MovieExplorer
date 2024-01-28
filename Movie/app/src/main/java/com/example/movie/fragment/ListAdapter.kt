package com.example.movie.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.data.Movie
import com.example.movie.R

class ListAdapter(private val isGridMode: Boolean) :
    RecyclerView.Adapter<ListAdapter.MovieViewHolder>() {
    private val list: ArrayList<Movie> = arrayListOf()
    lateinit var movieId: String

    interface OnItemClickListener {
        fun onItemClick(movie: Movie, position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: ListFragment) {
        onItemClickListener = listener
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfMovie: TextView = itemView.findViewById(R.id.movie_name)
        val langOfMovie: TextView = itemView.findViewById(R.id.movie_language)
        val rateOfMovie: TextView = itemView.findViewById(R.id.movie_rate)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val imageViewListFav: ImageView = itemView.findViewById(R.id.imageViewFavoriteIcon)

        fun bind(item: Movie) {
            nameOfMovie.text = item.originalTitle
            langOfMovie.text = item.originalLanguage
            rateOfMovie.text = item.voteAverage.toString() + "(" + item.voteCount.toString() + ")"

            Glide.with(photo).load("https://image.tmdb.org/t/p/original" + item.posterPath)
                .into(photo)

            movieId = item.id.toString()
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(item, bindingAdapterPosition)
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutId = if (isGridMode) R.layout.my_grid_item else R.layout.my_item
        val itemView = inflater.inflate(layoutId, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = list[position]
        if (currentItem.isVisible) {
            holder.imageViewListFav.visibility = View.VISIBLE
        } else {
            holder.imageViewListFav.visibility = View.INVISIBLE
        }
        holder.bind(currentItem)

    }

    fun setMovieList(movieList: ArrayList<Movie>) {
        list.clear()
        list.addAll(movieList)
        notifyDataSetChanged()
    }

}
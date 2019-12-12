package com.vferreirati.tormovies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieEntry
import kotlinx.android.synthetic.main.item_grid_movie.view.*
import javax.inject.Inject

class MovieSearchAdapter @Inject constructor(
    private val picasso: Picasso
) : RecyclerView.Adapter<MovieSearchAdapter.MovieSearchHolder>() {

    private var entries: List<MovieEntry>? = null
    private lateinit var callback: MovieAdapter.MovieCallback

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieSearchHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_movie, parent, false)
        return MovieSearchHolder(view)
    }

    override fun getItemCount(): Int = entries?.size ?: 0

    override fun onBindViewHolder(holder: MovieSearchHolder, position: Int) {
        val entry = entries!![position]

        entry.images.posterUrl?.let { url -> picasso.load(url)
            .placeholder(R.drawable.placeholder_movie_poster)
            .into(holder.imgMoviePoster)
        }

        holder.itemView.setOnClickListener { callback.onMovieSelected(entry) }
    }

    fun setEntries(movies: List<MovieEntry>) {
        entries = movies
        notifyDataSetChanged()
    }

    fun setCallback(callback: MovieAdapter.MovieCallback) {
        this.callback = callback
    }

    inner class MovieSearchHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgMoviePoster: AppCompatImageView = itemView.imgMoviePoster
    }


}
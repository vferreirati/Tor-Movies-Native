package com.vferreirati.tormovies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieEntry
import javax.inject.Inject

class MovieAdapter @Inject constructor(
    private val picasso: Picasso
) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    private var entries: List<MovieEntry>? = null
    private lateinit var callback: MovieCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_movie, parent, false)
        return MovieHolder(view)
    }

    override fun getItemCount(): Int = entries?.size ?: 0

    override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(entries!![position])

    fun setEntries(newEntries: List<MovieEntry>) {
        entries = newEntries
        notifyDataSetChanged()
    }

    fun setCallback(callback: MovieCallback) {
        this.callback = callback
    }

    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgMoviePoster: AppCompatImageView = itemView.findViewById(R.id.imgMoviePoster)
        private val txtMovieTitle: AppCompatTextView = itemView.findViewById(R.id.txtMovieName)

        fun bind(entry: MovieEntry) {
            txtMovieTitle.text = entry.title
            picasso.load(entry.posterImageUrl)
                .placeholder(R.drawable.placeholder_movie_poster)
                .into(imgMoviePoster)
            itemView.setOnClickListener { callback.onMovieSelected(entry) }
        }
    }

    interface MovieCallback {
        fun onMovieSelected(movieEntry: MovieEntry)
    }
}
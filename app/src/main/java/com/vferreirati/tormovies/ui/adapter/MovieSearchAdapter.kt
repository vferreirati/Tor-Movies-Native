package com.vferreirati.tormovies.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.squareup.picasso.Picasso
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieEntry
import kotlinx.android.synthetic.main.item_grid_movie.view.*
import kotlinx.android.synthetic.main.item_list_ad.view.*
import javax.inject.Inject
import kotlin.math.ceil


class MovieSearchAdapter @Inject constructor(
    private val picasso: Picasso
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var entries = mutableListOf<MovieEntry>()
    private lateinit var callback: MovieAdapter.MovieCallback

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_movie, parent, false)
            MovieSearchHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_ad, parent, false)
            AdHolder(view)
        }
    }

    override fun getItemCount(): Int {
        var additionalContent = 0
        if (entries.size > 0 && entries.size > AD_INTERVAL) {
            additionalContent = entries.size / AD_INTERVAL
        }
        return entries.size + additionalContent + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE) {
            val movieHolder = holder as MovieSearchHolder
            val entry = entries[getRealPosition(position)]

            entry.images.posterUrl?.let { url -> picasso.load(url)
                .placeholder(R.drawable.placeholder_movie_poster)
                .into(movieHolder.imgMoviePoster)
            }

            holder.itemView.setOnClickListener { callback.onMovieSelected(entry) }
        } else {
            val adHolder = holder as AdHolder
            val adRequest = AdRequest.Builder().build()
            adHolder.adView.loadAd(adRequest)
            adHolder.adView.adListener = object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    Log.e("Victor", errorCode.toString())
                }
            }
        }
    }

    private fun getRealPosition(position: Int): Int {
        val ceil = position - ceil(position / AD_INTERVAL.toDouble()).toInt()
        return ceil
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % AD_INTERVAL == 0) AD_VIEW_TYPE else ITEM_VIEW_TYPE
    }

    fun setEntries(movies: List<MovieEntry>) {
        entries.clear()
        entries.addAll(movies)
        notifyDataSetChanged()
    }

    fun setCallback(callback: MovieAdapter.MovieCallback) {
        this.callback = callback
    }

    inner class MovieSearchHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgMoviePoster: AppCompatImageView = itemView.imgMoviePoster
    }

    inner class AdHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val adView: AdView = itemView.adView
    }

    companion object {
        const val AD_VIEW_TYPE = 100
        const val ITEM_VIEW_TYPE = 200
        const val AD_INTERVAL = 11   // 10 items between ads
    }
}
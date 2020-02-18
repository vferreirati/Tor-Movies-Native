package com.vferreirati.tormovies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShimmerAdapter(
    private val layoutId: Int,
    private val itemCount: Int
) : RecyclerView.Adapter<ShimmerAdapter.ShimmerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ShimmerHolder(view)
    }

    override fun getItemCount(): Int = itemCount

    override fun onBindViewHolder(holder: ShimmerHolder, position: Int) { }

    inner class ShimmerHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
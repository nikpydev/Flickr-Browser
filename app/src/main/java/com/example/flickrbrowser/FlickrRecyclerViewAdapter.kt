package com.example.flickrbrowser

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickerImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>) :
    RecyclerView.Adapter<FlickerImageViewHolder>() {

    private val TAG = "FlickrRcyclrViewAdapt"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickerImageViewHolder {
        // Called by the layout manager when it needs a new view
        Log.d(TAG, ".onCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickerImageViewHolder(view)
    }

    override fun getItemCount(): Int {
//        Log.d(TAG, ".getItemCount called")
        return if (photoList.isNotEmpty()) photoList.size else 0
    }

    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }

    override fun onBindViewHolder(holder: FlickerImageViewHolder, position: Int) {
        // Called by the layout manager when it needs new data in an existing view

        val photoItem = photoList[position]
//        Log.d(TAG, ".onBindViewHolder: ${photoItem.title} → $position")
        Picasso.get().load(photoItem.image).error(R.drawable.broken_image)
            .placeholder(R.drawable.placeholder).into(holder.thumbnail)

        holder.title.text = photoItem.title
    }
}

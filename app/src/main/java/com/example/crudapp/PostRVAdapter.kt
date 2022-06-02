package com.example.crudapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

public class PostRVAdapter(
    private var postRVModelArrayList: ArrayList<PostRVModel>,
    private val context: Context,
    private var postClickInterface: PostClickInterface,
    private var lastPos: Int = -1
): RecyclerView.Adapter<PostRVAdapter.ItemViewHolder>() {





    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostRVAdapter.ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_rv_item1, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PostRVAdapter.ItemViewHolder, position: Int) {
        val postRVModel = postRVModelArrayList[position]
        holder.idItem.text = postRVModel.postTitle
        Picasso.get().load(postRVModel.postImg).into(holder.idImage)
        holder.idDesc.text = postRVModel.postDescription
        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position)
        holder.idItem.setOnClickListener { postClickInterface.onPostClick(position) }
    }

    override fun getItemCount(): Int {
        return postRVModelArrayList.size
    }
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idItem: TextView = itemView.findViewById(R.id.idTVPostTitle)
        val idImage: ImageView = itemView.findViewById(R.id.idIVPost)
        val idDesc: TextView = itemView.findViewById(R.id.idTVPostDesc)

    }
    private fun setAnimation(itemView: View, position: Int) {
        if (position > lastPos) {
            // on below line we are setting animation.
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            itemView.animation = animation
            lastPos = position
        }
    }
    interface PostClickInterface {
        fun onPostClick(position: Int)

    }

}



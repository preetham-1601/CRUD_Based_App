package com.example.crudapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

//import com.squareup.picasso.Picasso

class PostRVAdapter(private val context: Context,
                    private var postRVModelArrayList: ArrayList<Post>

): RecyclerView.Adapter<PostRVAdapter.ItemViewHolder>() {



    private var lastPos: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostRVAdapter.ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_rv_item1, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PostRVAdapter.ItemViewHolder, position: Int) {
        val post = postRVModelArrayList[position]
        holder.idItem.text = post.postTitle
        Picasso.get().load(post.postImage).into(holder.idImage)
        holder.idDesc.text = post.postDescription
        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position)
        holder.cardView.setOnClickListener {
            val bundle = bundleOf("image_url" to post.postImage,"title" to post.postTitle,"desc" to post.postDescription,"id" to post.postId)
            findNavController(holder.idItem).navigate(R.id.action_homeFragment_to_editPostFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return postRVModelArrayList.size
    }
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idItem: TextView = itemView.findViewById(R.id.idTVPostTitle)
        val idImage: ImageView = itemView.findViewById(R.id.idIVPost)
        val idDesc: TextView = itemView.findViewById(R.id.idTVPostDesc)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
    private fun setAnimation(itemView: View, position: Int) {
        if (position > lastPos) {
            // on below line we are setting animation.
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            itemView.animation = animation
            lastPos = position
        }
    }

}



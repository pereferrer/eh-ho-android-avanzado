package io.keepcoding.eh_ho.feature.topics.latestPosts.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.domain.LatestPost
import io.keepcoding.eh_ho.feature.topics.latestPosts.view.viewholder.latestNewsHolder

class LatestNewsAdapter (
    val latestNewsClickListener: ((LatestPost) -> Unit)? = null
) : RecyclerView.Adapter<latestNewsHolder>() {
    private val posts = mutableListOf<LatestPost>()

    private val listener: ((View) -> Unit) = {
        val post = it.tag as LatestPost
        latestNewsClickListener?.invoke(post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): latestNewsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_latest_news, parent, false)

        return latestNewsHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: latestNewsHolder, position: Int) {
        val post = posts[position]
        holder.post = post
        holder.itemView.setOnClickListener(listener)
    }

    fun setLatestNews(posts: List<LatestPost>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }
}
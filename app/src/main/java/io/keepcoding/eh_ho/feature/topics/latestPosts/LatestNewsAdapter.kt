package io.keepcoding.eh_ho.feature.topics.latestPosts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.domain.LatestPost
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_topic.view.labelDate
import java.util.*

class LatestNewsAdapter (
    val latestNewsClickListener: ((LatestPost) -> Unit)? = null
) : RecyclerView.Adapter<LatestNewsAdapter.latestNewsHolder>() {
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

    inner class latestNewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var post: LatestPost? = null
            set(value) {
                field = value

                with(itemView) {
                    tag = field
                    field?.let {
                        labelAuthor.text = it.username
                        labelPostContent.text = it.cooked
                        setTimeOffset(it.getTimeOffset())
                    }
                }
            }

        private fun setTimeOffset(timeOffset: LatestPost.TimeOffset) {
            val quantityString = when (timeOffset.unit) {
                Calendar.YEAR -> R.plurals.years
                Calendar.MONTH -> R.plurals.months
                Calendar.DAY_OF_MONTH -> R.plurals.days
                Calendar.HOUR -> R.plurals.hours
                else -> R.plurals.minutes
            }

            itemView.labelDate.text =
                if (timeOffset.amount != 0)
                    itemView.context.resources.getQuantityString(quantityString, timeOffset.amount, timeOffset.amount)
                else
                    itemView.context.resources.getString(R.string.minutes_zero)

        }
    }
}
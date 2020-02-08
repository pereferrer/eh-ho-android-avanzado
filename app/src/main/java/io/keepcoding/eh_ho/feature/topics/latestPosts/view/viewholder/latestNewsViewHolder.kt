package io.keepcoding.eh_ho.feature.topics.latestPosts.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.domain.LatestPost
import kotlinx.android.synthetic.main.item_latest_news.view.*
import kotlinx.android.synthetic.main.item_latest_news.view.labelAuthor
import kotlinx.android.synthetic.main.item_latest_news.view.labelPostContent
import java.util.*

class latestNewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var post: LatestPost? = null
        set(value) {
            field = value

            with(itemView) {
                tag = field
                field?.let {
                    labelAuthor.text = it.topic_title
                    labelPostContent.text = it.topic_slug
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

        itemView.labelDateLatestNews.text =
            if (timeOffset.amount != 0)
                itemView.context.resources.getQuantityString(quantityString, timeOffset.amount, timeOffset.amount)
            else
                itemView.context.resources.getString(R.string.minutes_zero)

    }
}
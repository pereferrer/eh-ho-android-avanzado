package io.keepcoding.eh_ho.domain

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class Topic(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("created_at")
    val fecha:String,
    @SerializedName("posts_count")
    val posts: Int = 0,
    @SerializedName("views")
    val views: Int = 0
) {
    companion object {
        const val MINUTES_MILLIS = 1000L * 60
        const val HOUR_MILLIS = MINUTES_MILLIS * 60
        const val DAY_MILLIS = HOUR_MILLIS * 24
        const val MONTH_MILLIS = DAY_MILLIS * 30
        const val YEAR_MILLIS = MONTH_MILLIS * 12
    }

    data class TimeOffset(val amount: Int, val unit: Int)

    fun getTimeOffset(dateToCompare: Date = Date()): TimeOffset {

        val date = fecha
            .replace("Z", "+0000")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
        val dateFormatted = dateFormat.parse(date) ?: Date()


        val current = dateToCompare.time
        val diff = current - dateFormatted.time

        val years = diff / YEAR_MILLIS
        if (years > 0) return TimeOffset(
            years.toInt(),
            Calendar.YEAR
        )

        val month = diff / MONTH_MILLIS
        if (month > 0) return TimeOffset(
            month.toInt(),
            Calendar.MONTH
        )

        val days = diff / DAY_MILLIS
        if (days > 0) return TimeOffset(
            days.toInt(),
            Calendar.DAY_OF_MONTH
        )

        val hours = diff / HOUR_MILLIS
        if (hours > 0) return TimeOffset(
            hours.toInt(),
            Calendar.HOUR
        )

        val minutes = diff / MINUTES_MILLIS
        if (minutes > 0) return TimeOffset(
            minutes.toInt(),
            Calendar.MINUTE
        )

        return TimeOffset(0, Calendar.MINUTE)
    }
}

data class Post(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("username")
    val username: String,
    @SerializedName("cooked")
    val cooked: String,
    @SerializedName("date")
    val date: Date = Date()
) {
    companion object {

        fun parsePosts(response: JSONObject): List<Post> {
            val jsonPosts = response.getJSONObject("post_stream")
                .getJSONArray("posts")

            val posts = mutableListOf<Post>()


            for (i in 0 until jsonPosts.length()) {
                val parsedPost =
                    parsePost(
                        jsonPosts.getJSONObject(i)
                    )
                posts.add(parsedPost)
            }

            return posts
        }

        fun parsePost(jsonObject: JSONObject): Post {
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()


            return Post(
                jsonObject.getInt("id").toString(),
                jsonObject.getString("username"),
                jsonObject.getString("cooked"),
                dateFormatted
            )
        }
    }

    data class TimeOffset(val amount: Int, val unit: Int)

    fun getTimeOffset(dateToCompare: Date = Date()): TimeOffset {
        val current = dateToCompare.time
        val diff = current - date.time

        val years = diff / Topic.YEAR_MILLIS
        if (years > 0) return TimeOffset(
            years.toInt(),
            Calendar.YEAR
        )

        val month = diff / Topic.MONTH_MILLIS
        if (month > 0) return TimeOffset(
            month.toInt(),
            Calendar.MONTH
        )

        val days = diff / Topic.DAY_MILLIS
        if (days > 0) return TimeOffset(
            days.toInt(),
            Calendar.DAY_OF_MONTH
        )

        val hours = diff / Topic.HOUR_MILLIS
        if (hours > 0) return TimeOffset(
            hours.toInt(),
            Calendar.HOUR
        )

        val minutes = diff / Topic.MINUTES_MILLIS
        if (minutes > 0) return TimeOffset(
            minutes.toInt(),
            Calendar.MINUTE
        )

        return TimeOffset(0, Calendar.MINUTE)
    }
}

data class LatestPost(
    @SerializedName("topic_id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("date")
    val date: Date = Date(),
    @SerializedName("topic_title")
    val topic_title: String,
    @SerializedName("topic_slug")
    val topic_slug: String,
    @SerializedName("created_at")
    val created_at:Date = Date(),
    @SerializedName("post_number")
    val post_number:Int,
    @SerializedName("score")
    val score:Double
) {
    companion object {

        fun parsePosts(response: JSONObject): List<LatestPost> {
            val jsonPosts = response.getJSONArray("latest_posts")


            val posts = mutableListOf<LatestPost>()


            for (i in 0 until jsonPosts.length()) {
                val parsedPost =
                    parsePost(
                        jsonPosts.getJSONObject(i)
                    )
                posts.add(parsedPost)
            }

            return posts
        }

        fun parsePost(jsonObject: JSONObject): LatestPost {
            val date = jsonObject.getString("created_at")
                .replace("Z", "+0000")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
            val dateFormatted = dateFormat.parse(date) ?: Date()


            return LatestPost(
                jsonObject.getInt("topic_id").toString(),
                dateFormatted,
                jsonObject.getString("topic_title"),
                jsonObject.getString("topic_slug"),
                dateFormatted,
                jsonObject.getInt("post_number"),
                jsonObject.getDouble("score")
            )
        }
    }




    data class TimeOffset(val amount: Int, val unit: Int)

    fun getTimeOffset(dateToCompare: Date = Date()): TimeOffset {
        val current = dateToCompare.time
        val diff = current - created_at.time

        val years = diff / Topic.YEAR_MILLIS
        if (years > 0) return TimeOffset(
            years.toInt(),
            Calendar.YEAR
        )

        val month = diff / Topic.MONTH_MILLIS
        if (month > 0) return TimeOffset(
            month.toInt(),
            Calendar.MONTH
        )

        val days = diff / Topic.DAY_MILLIS
        if (days > 0) return TimeOffset(
            days.toInt(),
            Calendar.DAY_OF_MONTH
        )

        val hours = diff / Topic.HOUR_MILLIS
        if (hours > 0) return TimeOffset(
            hours.toInt(),
            Calendar.HOUR
        )

        val minutes = diff / Topic.MINUTES_MILLIS
        if (minutes > 0) return TimeOffset(
            minutes.toInt(),
            Calendar.MINUTE
        )

        return TimeOffset(
            0,
            Calendar.MINUTE
        )
    }
}

package io.keepcoding.eh_ho

import io.keepcoding.eh_ho.domain.Topic
import org.json.JSONObject
import org.junit.Test
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import org.junit.Assert.*

class TopicModelTest {
    @Test
    fun getOffset_year_isCorrect() {
        val dateToCompare = formatDate("01/01/2020 10:00:00")
        val testTopic = Topic(
            title = "Test",
            date = formatDate("01/01/2019 10:00:00")
        )

        val offset = testTopic.getTimeOffset(dateToCompare)

        assertEquals("Amount comparison", 1, offset.amount)
        assertEquals("Unit comparison", Calendar.YEAR, offset.unit)
    }

    @Test
    fun getOffset_years_isCorrect() {
        val dateToCompare = formatDate("01/01/2020 10:00:00")
        val testTopic = Topic(
            title = "Test",
            date = formatDate("01/01/2017 10:00:00")
        )

        val offset = testTopic.getTimeOffset(dateToCompare)

        assertEquals("Amount comparison", 3, offset.amount)
        assertEquals("Unit comparison", Calendar.YEAR, offset.unit)
    }

    @Test
    fun getOffset_month_isCorrect() {
        val dateToCompare = formatDate("01/02/2019 10:00:00")
        val testTopic = Topic(
            title = "Test",
            date = formatDate("01/01/2019 10:00:00")
        )

        val offset = testTopic.getTimeOffset(dateToCompare)

        assertEquals("Amount comparison", 1, offset.amount)
        assertEquals("Unit comparison", Calendar.MONTH, offset.unit)
    }

    @Test
    fun getOffset_day_isCorrect() {
        val dateToCompare = formatDate("02/01/2019 10:00:00")
        val testTopic = Topic(
            title = "Test",
            date = formatDate("01/01/2019 10:00:00")
        )

        val offset = testTopic.getTimeOffset(dateToCompare)

        assertEquals("Amount comparison", 1, offset.amount)
        assertEquals("Unit comparison", Calendar.DAY_OF_MONTH, offset.unit)
    }

    @Test
    fun getOffset_hour_isCorrect() {
        val dateToCompare = formatDate("01/01/2019 11:00:00")
        val testTopic = Topic(
            title = "Test",
            date = formatDate("01/01/2019 10:00:00")
        )

        val offset = testTopic.getTimeOffset(dateToCompare)

        assertEquals("Amount comparison", 1, offset.amount)
        assertEquals("Unit comparison", Calendar.HOUR, offset.unit)
    }

    @Test
    fun getOffset_minute_isCorrect() {
        val dateToCompare = formatDate("01/01/2019 10:01:00")
        val testTopic = Topic(
            title = "Test",
            date = formatDate("01/01/2019 10:00:00")
        )

        val offset = testTopic.getTimeOffset(dateToCompare)

        assertEquals("Amount comparison", 1, offset.amount)
        assertEquals("Unit comparison", Calendar.MINUTE, offset.unit)
    }

    @Test
    fun getOffset_seconds_isCorrect() {
        val dateToCompare = formatDate("01/01/2019 10:00:30")
        val testTopic = Topic(
            title = "Test",
            date = formatDate("01/01/2019 10:00:00")
        )

        val offset = testTopic.getTimeOffset(dateToCompare)

        assertEquals("Amount comparison", 0, offset.amount)
        assertEquals("Unit comparison", Calendar.MINUTE, offset.unit)
    }

    @Test
    fun fromJson_isCorrect() {
        val stringTopic =
            "{\"id\":7,\"title\":\"Welcome to Discourse\",\"fancy_title\":\"Welcome to Discourse\",\"slug\":\"welcome-to-discourse\",\"posts_count\":1,\"reply_count\":0,\"highest_post_number\":1,\"image_url\":\"https://aws1.discourse-cdn.com/standard17/uploads/silmood/original/1X/1b44248c86c19414b907bf3b799ee95ae1681e68.gif\",\"created_at\":\"2019-12-12T01:41:28.809Z\",\"last_posted_at\":\"2019-12-12T01:41:28.854Z\",\"bumped\":true,\"bumped_at\":\"2019-12-12T01:50:52.431Z\",\"unseen\":false,\"pinned\":true,\"unpinned\":null,\"excerpt\":\"Welcome to Eh-Ho \\nThe first paragraph of this pinned topic will be visible as a welcome message to all new visitors on your homepage. It’s important! \\nEdit this into a brief description of your community: \\n\\nWho is it for&hellip;\",\"visible\":true,\"closed\":false,\"archived\":false,\"bookmarked\":null,\"liked\":null,\"views\":0,\"like_count\":0,\"has_summary\":false,\"archetype\":\"regular\",\"last_poster_username\":\"system\",\"category_id\":1,\"pinned_globally\":true,\"featured_link\":null,\"has_accepted_answer\":false,\"posters\":[{\"extras\":\"latest single\",\"description\":\"Original Poster, Most Recent Poster\",\"user_id\":-1,\"primary_group_id\":null}]}"
        val json = JSONObject(stringTopic)

        val topic = Topic.parseTopic(json)

        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val dateFormatted = formatter.format(topic.date)


        assertEquals("7", topic.id)
        assertEquals("Welcome to Discourse", topic.title)
        assertEquals(1, topic.posts)
        assertEquals(0, topic.views)
        assertEquals("11/12/2019 07:41:28", dateFormatted)
    }

    private fun formatDate(date: String): Date {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return formatter.parse(date)
            ?: throw IllegalArgumentException("Date $date has an incorrect format, try again with the next format dd/MM/yyyy hh:mm:ss")
    }

}
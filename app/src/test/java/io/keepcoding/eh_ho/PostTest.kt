package io.keepcoding.eh_ho

import io.keepcoding.eh_ho.domain.Post
import io.keepcoding.eh_ho.domain.Topic
import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class PostTest {

    @Test
    fun getOffset_year_isCorrect() {
        val dateToCompare = formatDate("01/01/2020 10:00:00")
        val testPost = Post(
            username = "Pere",
            date = formatDate("01/01/2019 10:00:00"),
            cooked = "Esto es un test de Post"
        )

        val offset = testPost.getTimeOffset(dateToCompare)

        Assert.assertEquals("Amount comparison", 1, offset.amount)
        Assert.assertEquals("Unit comparison", Calendar.YEAR, offset.unit)
    }

    @Test
    fun getOffset_month_isCorrect() {
        val dateToCompare = formatDate("01/02/2020 10:00:00")
        val testPost = Post(
            username = "Pere",
            date = formatDate("01/01/2020 10:00:00"),
            cooked = "Esto es un test de Post"
        )

        val offset = testPost.getTimeOffset(dateToCompare)

        Assert.assertEquals("Amount comparison", 1, offset.amount)
        Assert.assertEquals("Unit comparison", Calendar.MONTH, offset.unit)
    }


    private fun formatDate(date: String): Date {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return formatter.parse(date)
            ?: throw IllegalArgumentException("Date $date has an incorrect format, try again with the next format dd/MM/yyyy hh:mm:ss")
    }
}
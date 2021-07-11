package com.buslaev.monitoringcryptocurrency.screens.news

import android.os.Build
import com.buslaev.monitoringcryptocurrency.models.news.Data
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NewsCurrentItem(
    currentItem: Data
) {
    var title: String = ""
    var author: String = ""
    var date: String = ""

    init {
        title = currentItem.title
        author = currentItem.author.name
        date = convertDate(currentItem.published_at)
    }

    private fun convertDate(oldDate: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.getDefault())
            val date = LocalDate.parse(oldDate, inputFormatter)
            val formattedDate = outputFormatter.format(date)
            formattedDate
        } else {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val formatter = SimpleDateFormat("dd-MM-yyy")
            val formattedDate = formatter.format(parser.parse(oldDate))
            formattedDate
        }
    }
}
package com.buslaev.monitoringcryptocurrency.adapters.helpedModels

import android.os.Build
import android.text.Html
import android.text.Spanned
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
    var content: Spanned?

    init {
        title = currentItem.title
        author = currentItem.author.name
        date = convertDate(currentItem.published_at)
        content = convertContent(currentItem.content)
    }

    private fun convertContent(content: String): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(content)
        }
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
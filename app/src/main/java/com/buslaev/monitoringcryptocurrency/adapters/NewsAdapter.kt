package com.buslaev.monitoringcryptocurrency.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.models.news.Data
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var mList = emptyList<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = mList[position]
        holder.itemView.apply {
            news_title.text = currentItem.title
            news_author.text = currentItem.author.name
            news_published_time.text = currentItem.published_at
            val content = currentItem.content
            news_content.text = content
            news_read_more.setOnClickListener {
//                news_content_full.visibility = View.VISIBLE
//                news_content.visibility = View.GONE
//                news_content_full.text = content


                onItemClickListener?.let { it(currentItem) }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setList(list: List<Data>) {
        mList = list
        notifyDataSetChanged()
    }

    private var onItemClickListener: ((Data) -> Unit)? = null

    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }
}
package com.buslaev.monitoringcryptocurrency.adapters


import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.models.news.Data
import com.buslaev.monitoringcryptocurrency.screens.news.NewsCurrentItem
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
        val newsCurrentItem = NewsCurrentItem(currentItem)

        holder.itemView.apply {
            news_title.text = newsCurrentItem.title
            news_author.text = newsCurrentItem.author
            news_published_time.text = newsCurrentItem.date

//            val oldParams:ViewGroup.LayoutParams = news_content.layoutParams
//            news_read_more.setOnClickListener {
//
//                val params:ViewGroup.LayoutParams = news_content.layoutParams
//
//                if (params.height == ViewGroup.LayoutParams.MATCH_PARENT){
//                    params.height = 250
//                    news_content.layoutParams = params
//                } else {
//                    params.height = ViewGroup.LayoutParams.MATCH_PARENT
//                    news_content.layoutParams = params
//                }
//                onItemClickListener?.let { it(currentItem) }
//            }
            news_item_current.setOnClickListener {
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
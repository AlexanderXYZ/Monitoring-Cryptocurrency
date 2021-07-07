package com.buslaev.monitoringcryptocurrency.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Data
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import kotlinx.android.synthetic.main.crypto_item.view.*

class CryptoAdapter(
    val listener: OnItemClickListener
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    inner class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, differ.currentList[position])
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnItemClickListener {
        fun onItemClick(position: Int, selectedData: Data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crypto_item, parent, false)
        return CryptoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val currentCrypto = differ.currentList[position]
        holder.itemView.apply {
            title_crypto.text = currentCrypto.symbol
            price_crypto.text = String.format("%.5f", currentCrypto.metrics.market_data.price_usd)
            //Expand view
            expand_crypto.setOnClickListener {
                val viewLl = hidden_view_ll
                if (viewLl.visibility == View.VISIBLE) {
                    viewLl.visibility = View.GONE
                    expand_crypto.setImageResource(R.drawable.ic_expand_more)
                } else {
                    viewLl.visibility = View.VISIBLE
                    expand_crypto.setImageResource(R.drawable.ic_expand_less)
                }
                //onItemClickListener?.let { it(currentCrypto) }
            }
            crypto_metrics.setOnClickListener {

            }
            crypto_profile.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("symbol", currentCrypto.symbol)
                APP_ACTIVITY.navController.navigate(
                    R.id.action_allCryptoFragment_to_profileFragment,
                    bundle
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Data) -> Unit)? = null

    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }
}
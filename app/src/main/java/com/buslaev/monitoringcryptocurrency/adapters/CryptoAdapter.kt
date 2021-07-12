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
import com.buslaev.monitoringcryptocurrency.screens.currentItems.CryptoCurrentItem
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.SYMBOL_KEY
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

    private val mapViewState = hashMapOf<String, Boolean>()
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val currentPosition = differ.currentList[position]
        val cryptoCurrentItem = CryptoCurrentItem(currentPosition)

        holder.itemView.apply {
            title_crypto.text = cryptoCurrentItem.title
            symbol_crypto.text = cryptoCurrentItem.symbol
            price_crypto.text = cryptoCurrentItem.price
            percent_change_crypto.apply {
                text = cryptoCurrentItem.percentChange
                setTextColor(cryptoCurrentItem.percentColor)
            }
            percent.setTextColor(cryptoCurrentItem.percentColor)

            if (mapViewState[currentPosition.id] == true) {
                hidden_view_ll.visibility = View.VISIBLE
                expand_crypto.setImageResource(R.drawable.ic_expand_less)
            }
            //Expand view
            fixed_view.setOnClickListener {
                val viewLl = hidden_view_ll
                if (viewLl.visibility == View.VISIBLE) {
                    viewLl.visibility = View.GONE
                    mapViewState[currentPosition.id] = false
                    expand_crypto.setImageResource(R.drawable.ic_expand_more)
                } else {
                    viewLl.visibility = View.VISIBLE
                    mapViewState[currentPosition.id] = true
                    expand_crypto.setImageResource(R.drawable.ic_expand_less)
                }
                onItemClickListener?.let { it(currentPosition) }
            }
            //Navigate to metrics
            crypto_metrics.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(SYMBOL_KEY, cryptoCurrentItem.symbol)
                APP_ACTIVITY.navController.navigate(
                    R.id.action_allCryptoFragment_to_metricsFragment,
                    bundle
                )
            }
            //Navigate to profile
            crypto_profile.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(SYMBOL_KEY, cryptoCurrentItem.symbol)
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
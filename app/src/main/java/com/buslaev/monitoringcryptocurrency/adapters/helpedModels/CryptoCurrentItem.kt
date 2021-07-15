package com.buslaev.monitoringcryptocurrency.adapters.helpedModels

import android.graphics.Color
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Data

class CryptoCurrentItem(
    currentItem: Data
) {
    var title: String = ""
    var symbol: String = ""
    var price: String = ""
    var percentChange: String = ""
    var percentColor: Int = 0

    init {
        this.title = currentItem.slug
        this.symbol = currentItem.symbol
        setTransformedPrice(currentItem.metrics.market_data.price_usd)
        setTransformedPercentChange(currentItem.metrics.market_data.percent_change_usd_last_24_hours)
    }

    private fun setTransformedPrice(price: Double) {
        this.price = if (price < 5) {
            "$"+String.format("%.5f", price)
        } else {
            "$"+String.format("%.2f", price)
        }
    }

    private fun setTransformedPercentChange(percentChange: Double) {
        this.percentChange = String.format("%.2f", percentChange) + "%"
        this.percentColor = if (percentChange < 0) {
            Color.RED
        } else {
            Color.GREEN
        }
    }
}
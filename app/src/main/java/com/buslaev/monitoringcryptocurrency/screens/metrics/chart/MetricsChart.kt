package com.buslaev.monitoringcryptocurrency.screens.metrics.chart

import android.content.Context
import android.graphics.Color
import android.text.format.DateFormat
import com.buslaev.monitoringcryptocurrency.screens.metrics.MetricsFragment
import com.buslaev.monitoringcryptocurrency.screens.metrics.MetricsFragment.Range.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.*
import kotlin.collections.ArrayList

class MetricsChart(
    private val context: Context,
    private val listValues: List<List<Double>>,
    private val range: MetricsFragment.Range
) {
    private val nameLabel = "Label"
    private val entries = ArrayList<Entry>()
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineData: LineData
    private lateinit var lineChart: LineChart

    init {
        setEntries()
        setLineDataSet()
        setLineData()
        setLineChart()
    }

    fun getLineChart(): LineChart {
        return this.lineChart
    }

    private fun setEntries() {
        var x = 0F
        for (item in listValues) {
            entries.add(Entry(x, item[4].toFloat()))
            x += 1F
        }
    }

    private fun setLineDataSet() {
        val lineDataSet = LineDataSet(entries, nameLabel)
        lineDataSet.apply {
            color = Color.GREEN
            lineWidth = 1.5F
            setDrawCircles(false)
            setDrawValues(false)
        }
        this.lineDataSet = lineDataSet
    }

    private fun setLineData() {
        this.lineData = LineData(lineDataSet)
    }

    private fun setLineChart() {
        val lineChart = LineChart(context)
        lineChart.apply {
            data = lineData

            axisLeft.setDrawLabels(false)
            axisRight.textColor = Color.WHITE

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                valueFormatter = MyXAxisFormatter()
            }
            legend.isEnabled = false
            description.isEnabled = false
            isScaleYEnabled = false
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    //mBinding.currentPriceChart.text = "$${e?.y.toString()}"
                }

                override fun onNothingSelected() {}
            })
            invalidate()
        }
        this.lineChart = lineChart
    }

    inner class MyXAxisFormatter : ValueFormatter() {
        private val xAxisDate = ArrayList<String>()
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            for (item in listValues) {
                val cal = Calendar.getInstance(Locale.getDefault())
                cal.timeInMillis = item[0].toLong()
                val date = when (range) {
                    HOUR -> DateFormat.format("HH:mm", cal).toString()
                    DAY -> DateFormat.format("HH:mm", cal).toString()
                    MONTH -> DateFormat.format("dd-MM", cal).toString()
                    YEAR -> DateFormat.format("yyyy-dd-MM", cal).toString()
                    ALL -> DateFormat.format("yyyy-MM", cal).toString()
                }
                xAxisDate.add(date)
            }
            return xAxisDate.getOrNull(value.toInt()) ?: value.toString()
        }
    }

}

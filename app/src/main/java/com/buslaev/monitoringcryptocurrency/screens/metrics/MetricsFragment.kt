package com.buslaev.monitoringcryptocurrency.screens.metrics

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.buslaev.monitoringcryptocurrency.MainActivity
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.adapters.helpedModels.CryptoIndicators
import com.buslaev.monitoringcryptocurrency.databinding.FragmentMetricsBinding
import com.buslaev.monitoringcryptocurrency.models.metrics.Data
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.screens.metrics.MetricsFragment.Range.*
import com.buslaev.monitoringcryptocurrency.utilits.APPLICATION_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MetricsFragment : Fragment() {

    private var _binding: FragmentMetricsBinding? = null
    private val mBinding get() = _binding!!

    private val mViewModel: MetricsViewModel by viewModels()
    private lateinit var mObserver: Observer<Resource<MetricsResponse>>

    private lateinit var mListValues: List<List<Double>>

    private lateinit var selectedItemPeriod: TextView
    private var currentChart: Range = MONTH

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMetricsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cryptoIndicators = arguments?.getSerializable("crypto") as CryptoIndicators
        setArgumentsFromCurrentCrypto(cryptoIndicators)
        mViewModel.setCurrentSymbol(cryptoIndicators.symbol)

        if (savedInstanceState != null) {
            loadSavedData()
        } else {
            loadNewData()
        }

        initObserver()
        initPeriodButtons()
    }

    private fun setArgumentsFromCurrentCrypto(cryptoIndicators: CryptoIndicators) {
        mBinding.metricsTitle.text =
            cryptoIndicators.title.replaceFirstChar { it.uppercase() } + "(${cryptoIndicators.symbol})"
        mBinding.metricsCurrentPrice.text = cryptoIndicators.price
        mBinding.metricsCurrentPriceChange.text = cryptoIndicators.percent
        mBinding.metricsCurrentPriceChange.setTextColor(cryptoIndicators.colorPercent)
    }

    private fun loadSavedData() {
        val savedData = arguments?.getSerializable("savedData") as MetricsCurrent
        currentChart = savedData.currentChart
        selectedItemPeriod = when (currentChart) {
            HOUR -> mBinding.metricsPeriod1h
            DAY -> mBinding.metricsPeriod1d
            MONTH -> mBinding.metricsPeriod1m
            YEAR -> mBinding.metricsPeriod1y
            ALL -> mBinding.metricsPeriodAll
        }
    }

    private fun loadNewData() {
        getMetricsData(MONTH)
        selectedItemPeriod = mBinding.metricsPeriod1m
    }

    override fun onPause() {
        super.onPause()
        val savedData = MetricsCurrent(currentChart)
        arguments?.putSerializable("savedData", savedData)
    }

    private fun initPeriodButtons() {
        mBinding.metricsPeriod1h.setOnClickListener {
            getMetricsData(HOUR)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriod1h)
        }
        mBinding.metricsPeriod1d.setOnClickListener {
            getMetricsData(DAY)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriod1d)
        }
        mBinding.metricsPeriod1m.setOnClickListener {
            getMetricsData(MONTH)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriod1m)
        }
        mBinding.metricsPeriod1y.setOnClickListener {
            getMetricsData(YEAR)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriod1y)
        }
        mBinding.metricsPeriodAll.setOnClickListener {
            getMetricsData(ALL)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriodAll)
        }
    }

    private fun getMetricsData(range: Range) {
        currentChart = range
        mViewModel.getMetrics(range)
    }

    private fun setBackgroundSelectedPeriodItem(textView: TextView) {
        selectedItemPeriod.setBackgroundResource(0)
        selectedItemPeriod = textView
        selectedItemPeriod.setBackgroundResource(R.drawable.background_metrics_period_selected)
    }


    private fun initChart() {
        val entries = getEntries()
        val lineDataSet = getLineDataSet(entries)
        val lineData = LineData(lineDataSet)
        setChart(lineData)

    }

    private fun getEntries(): ArrayList<Entry> {
        val entries = ArrayList<Entry>()
        var x = 0F
        for (item in mListValues) {
            entries.add(Entry(x, item[4].toFloat()))
            x += 1F
        }
        return entries
    }

    private fun getLineDataSet(entries: ArrayList<Entry>): LineDataSet {
        val lineDataSet = LineDataSet(entries, "Label")
        lineDataSet.apply {
            color = Color.GREEN
            lineWidth = 1.5F
            setDrawCircles(false)
            setDrawValues(false)
        }
        return lineDataSet
    }

    private fun setChart(lineData: LineData) {
        mBinding.chart.apply {
            data = lineData

            axisLeft.setDrawLabels(false)
            axisRight.textColor = Color.WHITE

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                valueFormatter = MyXAxisFormatter(mListValues)
            }
            legend.isEnabled = false
            description.isEnabled = false
            isScaleYEnabled = false
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    mBinding.currentPriceChart.text = "$${e?.y.toString()}"
                }

                override fun onNothingSelected() {}
            })
            invalidate()
        }
    }


    inner class MyXAxisFormatter(
        private val listValues: List<List<Double>>
    ) : ValueFormatter() {
        private val days = ArrayList<String>()
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            for (item in listValues) {
                val cal = Calendar.getInstance(Locale.getDefault())
                cal.timeInMillis = item[0].toLong()
                val date = when (currentChart) {
                    HOUR -> DateFormat.format("HH:mm", cal).toString()
                    DAY -> DateFormat.format("HH:mm", cal).toString()
                    MONTH -> DateFormat.format("dd-MM", cal).toString()
                    YEAR -> DateFormat.format("yyyy-dd-MM", cal).toString()
                    ALL -> DateFormat.format("yyyy-MM", cal).toString()
                }
                days.add(date)
            }
            return days.getOrNull(value.toInt()) ?: value.toString()
        }
    }

    private fun initObserver() {
        mObserver = Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { metricsResponse ->
                        mListValues = metricsResponse.data.values
                        initChart()
                    }
                    mBinding.loadingChart.visibility = View.GONE
                }
                is Resource.Loading -> {
                    mBinding.loadingChart.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "Error: $message", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        mViewModel.metrics.observe(viewLifecycleOwner, mObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class Range {
        HOUR, DAY, MONTH, YEAR, ALL
    }


}
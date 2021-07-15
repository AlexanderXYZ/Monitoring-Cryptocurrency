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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.databinding.FragmentMetricsBinding
import com.buslaev.monitoringcryptocurrency.models.metrics.Data
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
import com.buslaev.monitoringcryptocurrency.screens.metrics.MetricsFragment.Range.*
import com.buslaev.monitoringcryptocurrency.utilits.APPLICATION_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import com.buslaev.monitoringcryptocurrency.utilits.SYMBOL_KEY
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MetricsFragment : Fragment() {

    private var _binding: FragmentMetricsBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mViewModel: MetricsViewModel
    private lateinit var mObserver: Observer<Resource<MetricsResponse>>

    private lateinit var currentCrypto: Data
    private val listValues: List<List<Double>> get() = currentCrypto.values
    private val CLOSE = 4

    private lateinit var selectedItemPeriod: TextView
    private var currentChart: Range = MONTH

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMetricsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        if (savedInstanceState != null) {
//
//        } else {
//            currentChart = MONTH
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val symbol = arguments?.getString(SYMBOL_KEY) ?: "btc"

        mViewModel =
            ViewModelProvider(this, MetricsViewModelFactory(APPLICATION_ACTIVITY, symbol)).get(
                MetricsViewModel::class.java
            )
        if (savedInstanceState != null) {
            val savedData = arguments?.getSerializable("savedData") as MetricsCurrent
            currentChart = savedData.currentChart
            selectedItemPeriod = when (currentChart) {
                HOUR -> mBinding.metricsPeriod1h
                DAY -> mBinding.metricsPeriod1d
                MONTH -> mBinding.metricsPeriod1m
                YEAR -> mBinding.metricsPeriod1y
                ALL -> mBinding.metricsPeriodAll
            }
        } else {
            currentChart = MONTH
            selectedItemPeriod = mBinding.metricsPeriod1m
        }
        mViewModel.getMetrics(currentChart)

        initObserver()
        initPeriodButtons()
    }

//    override fun onStart() {
//        super.onStart()
//    }

    override fun onPause() {
        super.onPause()
        val savedData = MetricsCurrent(currentChart)
        arguments?.putSerializable("savedData", savedData)
    }

    private fun initPeriodButtons() {
        mBinding.metricsPeriod1h.setOnClickListener {
            currentChart = HOUR
            mViewModel.getMetrics(HOUR)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriod1h)
        }
        mBinding.metricsPeriod1d.setOnClickListener {
            currentChart = DAY
            mViewModel.getMetrics(DAY)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriod1d)
        }
        mBinding.metricsPeriod1m.setOnClickListener {
            currentChart = MONTH
            mViewModel.getMetrics(MONTH)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriod1m)
        }
        mBinding.metricsPeriod1y.setOnClickListener {
            currentChart = YEAR
            mViewModel.getMetrics(YEAR)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriod1y)
        }
        mBinding.metricsPeriodAll.setOnClickListener {
            currentChart = ALL
            mViewModel.getMetrics(ALL)
            setBackgroundSelectedPeriodItem(mBinding.metricsPeriodAll)
        }
    }

    private fun setBackgroundSelectedPeriodItem(textView: TextView) {
        selectedItemPeriod.setBackgroundResource(0)
        selectedItemPeriod = textView
        selectedItemPeriod.setBackgroundResource(R.drawable.background_metrics_period_selected)
    }


    private fun initChart() {
        val entries = ArrayList<Entry>()
        var x = 0F
        for (item in listValues) {
            entries.add(Entry(x, item[CLOSE].toFloat()))
            x += 1F
        }

        val lineDataSet = LineDataSet(entries, "Label")
        lineDataSet.apply {
            color = Color.GREEN
            lineWidth = 1.5F
            setDrawCircles(false)
            setDrawValues(false)
        }

        val lineData = LineData(lineDataSet)
        mBinding.chart.apply {
            data = lineData

            axisLeft.setDrawLabels(false)
            axisRight.textColor = Color.WHITE

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                valueFormatter = MyXAxisFormatter(listValues)
            }
            animateX(1000)
            legend.isEnabled = false
            description.isEnabled = false
            isScaleYEnabled = false
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
                        currentCrypto = metricsResponse.data
                        initChart()
                        mBinding.metricsTitle.text =
                            currentCrypto.name + "(${currentCrypto.symbol})"

                    }
                }
                is Resource.Loading -> {

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
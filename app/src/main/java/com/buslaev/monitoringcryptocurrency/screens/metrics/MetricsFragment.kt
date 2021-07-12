package com.buslaev.monitoringcryptocurrency.screens.metrics

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.buslaev.monitoringcryptocurrency.databinding.FragmentMetricsBinding
import com.buslaev.monitoringcryptocurrency.models.metrics.Data
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponse
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


class MetricsFragment : Fragment() {

    private var _binding: FragmentMetricsBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mViewModel: MetricsViewModel
    private lateinit var mObserver: Observer<Resource<MetricsResponse>>

    private lateinit var currentCrypto: Data
    private val listValues: List<List<Double>> get() = currentCrypto.values
    private val CLOSE = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMetricsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        val symbol = arguments?.getString(SYMBOL_KEY) ?: "btc"
        mViewModel = ViewModelProvider(this, MetricsViewModelFactory(symbol)).get(
            MetricsViewModel::class.java
        )
        mViewModel.getMetrics1m()

        initObserver()
        initPeriodButtons()
    }

    private fun initPeriodButtons() {
        mBinding.metricsPeriod1h.setOnClickListener {
            mViewModel.getMetrics1h()
            initChart()
        }
        mBinding.metricsPeriod1d.setOnClickListener {
            mViewModel.getMetrics1d()
            initChart()
        }
        mBinding.metricsPeriod1m.setOnClickListener {
            mViewModel.getMetrics1m()
            initChart()
        }
        mBinding.metricsPeriod1y.setOnClickListener {
            mViewModel.getMetrics1y()
            initChart()
        }
        mBinding.metricsPeriodAll.setOnClickListener {
            mViewModel.getMetricsAll()
            initChart()
        }
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
            valueTextColor = Color.WHITE
            circleRadius = 0F
            lineWidth = 1.5F
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
            legend.isEnabled = false
            description.isEnabled = false
            isScaleYEnabled = false
            invalidate()
        }
    }


    class MyXAxisFormatter(private val listValues: List<List<Double>>) : ValueFormatter() {
        private val days = ArrayList<String>()
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            for (item in listValues) {
                val cal = Calendar.getInstance(Locale.getDefault())
                cal.timeInMillis = item[0].toLong()
                val date = DateFormat.format("dd-MM", cal).toString()
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
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
            }
        }
        mViewModel.metrics.observe(viewLifecycleOwner, mObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
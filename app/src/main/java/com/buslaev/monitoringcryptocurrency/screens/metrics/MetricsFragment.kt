package com.buslaev.monitoringcryptocurrency.screens.metrics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.databinding.FragmentMetricsBinding
import com.buslaev.monitoringcryptocurrency.models.metrics.MetricsResponce
import com.buslaev.monitoringcryptocurrency.utilits.Resource


class MetricsFragment : Fragment() {

    private var _binding: FragmentMetricsBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mViewModel: MetricsViewModel
    private lateinit var mObserver: Observer<Resource<MetricsResponce>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMetricsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mViewModel = ViewModelProvider(this).get(MetricsViewModel::class.java)
        val symbol = arguments?.getString("symbol") ?: "btc"
        mViewModel.getMetrics(symbol)

        initObserver()

    }

    private fun initObserver() {
        mObserver = Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

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
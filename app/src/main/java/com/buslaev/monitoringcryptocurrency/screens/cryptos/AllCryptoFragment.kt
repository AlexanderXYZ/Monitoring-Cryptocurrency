package com.buslaev.monitoringcryptocurrency.screens.cryptos

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.adapters.CryptoAdapter
import com.buslaev.monitoringcryptocurrency.databinding.FragmentAllCryptoBinding
import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Data
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.android.synthetic.main.fragment_all_crypto.*


class AllCryptoFragment : Fragment() {

    private var _binding: FragmentAllCryptoBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mViewModel: CryptoViewModel
    private lateinit var mAdapter: CryptoAdapter

    private lateinit var mObserver: Observer<Resource<CryptoResponse>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllCryptoBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializations()
    }

    private fun initializations() {
        mViewModel = ViewModelProvider(this).get(CryptoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = CryptoAdapter(mBinding.cryptosRecyclerView)
    }
    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)

        initRecyclerView()

        mViewModel.startUpdatesData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_crypto_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_menu -> {
                mViewModel.getAllCrypto()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        mBinding.cryptosRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
            itemAnimator?.changeDuration = 0
        }

        mObserver = Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { cryptoResponse ->
                        mAdapter.differ.submitList(cryptoResponse.data)
                        mBinding.loadingProgressBar.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(activity, "Error: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
        mViewModel.allCrypto.observe(viewLifecycleOwner, mObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mViewModel.stopUpdatesData()
    }
}
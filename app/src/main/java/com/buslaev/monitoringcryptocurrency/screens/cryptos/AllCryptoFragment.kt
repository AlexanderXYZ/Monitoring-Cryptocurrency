package com.buslaev.monitoringcryptocurrency.screens.cryptos

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.adapters.CryptoAdapter
import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Data
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.android.synthetic.main.fragment_all_crypto.*


class AllCryptoFragment : Fragment(), CryptoAdapter.OnItemClickListener {

    private lateinit var mViewModel: CryptoViewModel
    private lateinit var mAdapter: CryptoAdapter

    private lateinit var mObserver: Observer<Resource<CryptoResponse>>

    private val TAG = "allCrypto"
    private val timerCount = 10000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_crypto, container, false)
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        mViewModel = ViewModelProvider(this).get(CryptoViewModel::class.java)
        mViewModel.getAllCrypto()

        initRecyclerView()

        //InitTimer
        updateCrypto()


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

    fun updateCrypto() {
        object : CountDownTimer(timerCount, 1000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                mViewModel.getAllCrypto()
                updateCrypto()
            }
        }.start()
    }

    private fun initRecyclerView() {
        mAdapter = CryptoAdapter(this)
        cryptos_recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        mObserver = Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { cryptoResponse ->
                        mAdapter.differ.submitList(cryptoResponse.data)
                        loading_progressBar.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "Error: $message")
                    }
                }
                is Resource.Loading -> {
                    loading_progressBar.visibility = View.VISIBLE
                }
            }
        }
        mViewModel.allCrypto.observe(viewLifecycleOwner, mObserver)
    }

    override fun onItemClick(position: Int, selectedData: Data) {
//        val builder = AlertDialog.Builder(APP_ACTIVITY)
//            .setTitle("Add ${selectedData.symbol} to favorite list?")
//            .setPositiveButton(getString(R.string.add_data_yes)) { dialog, which ->
//                mViewModel.addData(selectedData) {
//                    Toast.makeText(
//                        APP_ACTIVITY,
//                        getString(R.string.add_data_success),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }.setNegativeButton(getString(R.string.add_data_no)) { dialog, which -> }
//        builder.show()

    }
}
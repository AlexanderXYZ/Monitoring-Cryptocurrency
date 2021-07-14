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
import com.buslaev.monitoringcryptocurrency.databinding.FragmentAllCryptoBinding
import com.buslaev.monitoringcryptocurrency.models.allCrypto.CryptoResponse
import com.buslaev.monitoringcryptocurrency.models.allCrypto.Data
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.android.synthetic.main.fragment_all_crypto.*


class AllCryptoFragment : Fragment(), CryptoAdapter.OnItemClickListener {

    private var _binding: FragmentAllCryptoBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mViewModel: CryptoViewModel
    private lateinit var mAdapter: CryptoAdapter

    private lateinit var mObserver: Observer<Resource<CryptoResponse>>

    private val TAG = "allCrypto"
    private val timerCount = 10000L
    private val timerInterval = 1000L

    private lateinit var mTimer: TimerUpdateCrypto

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
        mAdapter = CryptoAdapter(this)
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)

        initRecyclerView()
        //InitTimer
        startUpdates()
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


    inner class TimerUpdateCrypto : CountDownTimer(timerCount, timerInterval) {
        override fun onTick(p0: Long) {}

        override fun onFinish() {
            mViewModel.getAllCrypto()
            startUpdates()
        }
    }

    fun startUpdates() {
        mTimer = TimerUpdateCrypto()
        mTimer.start()
    }

    private fun cancelUpdates() {
        mTimer.cancel()
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
                        Log.e(TAG, "Error: $message")
                    }
                }
                is Resource.Loading -> {
//                    loading_progressBar.visibility = View.VISIBLE
                }
            }
        }
        mViewModel.allCrypto.observe(viewLifecycleOwner, mObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cancelUpdates()
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
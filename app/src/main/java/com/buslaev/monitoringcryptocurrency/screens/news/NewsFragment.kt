package com.buslaev.monitoringcryptocurrency.screens.news

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.adapters.NewsAdapter
import com.buslaev.monitoringcryptocurrency.models.news.NewsResponse
import com.buslaev.monitoringcryptocurrency.screens.cryptos.CryptoViewModel
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.android.synthetic.main.fragment_all_crypto.*
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : Fragment() {

    private lateinit var mViewModel: NewsViewModel

    private lateinit var mAdapter: NewsAdapter
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mObserver: Observer<Resource<NewsResponse>>

    private val TAG = "newsError"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        mViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        initRecyclerView()

        mAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putSerializable("url", it)
            findNavController().navigate(R.id.action_newsFragment_to_webNewsFragment, bundle)
        }

    }

    private fun initRecyclerView() {
        mAdapter = NewsAdapter()
        mRecyclerView = news_recyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        mObserver = Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->
                        mAdapter.setList(newsResponse.data)
                        news_progressBar.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "Error: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
        mViewModel.news.observe(viewLifecycleOwner, mObserver)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_crypto_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_menu -> {
                mViewModel.getNews()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
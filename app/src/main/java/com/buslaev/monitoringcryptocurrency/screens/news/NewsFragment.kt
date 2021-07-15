package com.buslaev.monitoringcryptocurrency.screens.news

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.adapters.NewsAdapter
import com.buslaev.monitoringcryptocurrency.databinding.FragmentNewsBinding
import com.buslaev.monitoringcryptocurrency.models.news.NewsResponse
import com.buslaev.monitoringcryptocurrency.screens.cryptos.CryptoViewModel
import com.buslaev.monitoringcryptocurrency.utilits.Resource
import kotlinx.android.synthetic.main.fragment_all_crypto.*
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var mViewModel: NewsViewModel

    private lateinit var mAdapter: NewsAdapter
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mObserver: Observer<Resource<NewsResponse>>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)

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
                        Toast.makeText(activity, "Error: $message", Toast.LENGTH_LONG).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
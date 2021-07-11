package com.buslaev.monitoringcryptocurrency.screens.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import androidx.fragment.app.Fragment
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.buslaev.monitoringcryptocurrency.R
import com.buslaev.monitoringcryptocurrency.databinding.FragmentWebNewsBinding
import com.buslaev.monitoringcryptocurrency.models.news.Data


class WebNewsFragment : Fragment() {

    private var _binding: FragmentWebNewsBinding? = null
    private val mBinding get() = _binding!!

    //val args: WebNewsFragmentArgs by navArgs()
    private lateinit var news: Data
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebNewsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        news = arguments?.getSerializable("url") as Data

        webView = mBinding.newsWebView
        //val data = args.newsData
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(news.url)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.web_news_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_news_menu -> {
                shareUrl()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareUrl() {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, news.url)
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        webView.destroy()
    }
}
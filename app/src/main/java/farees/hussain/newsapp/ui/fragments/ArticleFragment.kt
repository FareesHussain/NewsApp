package farees.hussain.newsapp.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import farees.hussain.newsapp.R
import farees.hussain.newsapp.databinding.FragmentArticleBinding
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.coroutines.runBlocking

class ArticleFragment : Fragment() {

    private lateinit var binding : FragmentArticleBinding
    private val args : ArticleFragmentArgs by navArgs()
    private lateinit var webView : WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article,container,false)
        var article = args.article
        webView = binding.webView
        runBlocking { showWebView(article.url) }
        return binding.root
    }

    private suspend fun showWebView(url : String){
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl(url)

        webView.webViewClient = object: WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.i("article fragment", "started")
                binding.progressBar3.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.i("article fragment", "finished")
                binding.progressBar3.visibility = View.GONE
                webView.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }
        }
    }


}
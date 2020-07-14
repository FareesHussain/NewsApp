package farees.hussain.newsapp.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import farees.hussain.newsapp.R
import farees.hussain.newsapp.databinding.FragmentArticleBinding
import farees.hussain.newsapp.ui.activities.NewsActivity
import farees.hussain.newsapp.ui.activities.NewsViewModel
import kotlinx.coroutines.runBlocking

class ArticleFragment : Fragment() {

    private lateinit var binding : FragmentArticleBinding
    private lateinit var viewModel : NewsViewModel
    private val args : ArticleFragmentArgs by navArgs()
    private lateinit var webView : WebView
    private lateinit var url : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article,container,false)
        viewModel = (activity as NewsActivity).viewModel
        setHasOptionsMenu(true)
        var article = args.article
        url = article?.url.toString()
        binding.fab.setOnClickListener {
            try{
                viewModel.saveArticle(article)
                Snackbar.make(this.requireView(),"Article saved successfully", Snackbar.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(this.context,e.toString(),Toast.LENGTH_SHORT).show()
            }
        }
        webView = binding.webView
        runBlocking { showWebView(article.url) }
        return binding.root
    }

    private suspend fun showWebView(url: String?){
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_article_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share -> {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type="text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT,url );
                startActivity(Intent.createChooser(shareIntent,getString(R.string.app_name)))
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
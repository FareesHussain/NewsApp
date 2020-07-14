package farees.hussain.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import farees.hussain.newsapp.R
import farees.hussain.newsapp.adapter.NewsAdapter
import farees.hussain.newsapp.databinding.FragmentSearchNewsBinding
import farees.hussain.newsapp.ui.activities.NewsActivity
import farees.hussain.newsapp.ui.activities.NewsViewModel
import farees.hussain.newsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_search_news.view.*

class SearchNewsFragment : Fragment() {

    private lateinit var binding : FragmentSearchNewsBinding
    private lateinit var viewModel : NewsViewModel
    private lateinit var newsAdapter : NewsAdapter
    private val TAG :String = "Search News Fragment"
    private val args : SearchNewsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_news,container,false)
        viewModel = (activity as NewsActivity).viewModel
        setHasOptionsMenu(true)
        newsAdapter = NewsAdapter()
        if(args.showercyclerview){
            setupRecyclerView()
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / 20 +2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Toast.makeText(this.activity,message,Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        return binding.root
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
        newsAdapter.setOnItemClickListener { article ->
            findNavController().navigate(SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(article))
        }
    }

    private fun hideProgressBar(){
        binding.progressBar2.visibility = View.GONE
        binding.rvSearchNews.visibility = View.VISIBLE
        isLoading = false
    }
    private fun showProgressBar(){
        binding.progressBar2.visibility = View.VISIBLE
        binding.rvSearchNews.visibility = View.VISIBLE
        isLoading = true
    }


    var isLoading  = false
    var isLastPage  = false
    var isScrolling  = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotAtLastPage = !isLoading && !isLastPage
            val isAtLastPage = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotAtLastPage && isAtLastPage && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.searchNews(query)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
        val sv = menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView
        sv.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNews(query!!)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    lateinit var query : String

    fun searchNews(query :String){
        this.query = query
        viewModel.searchNews(query)
        setupRecyclerView()
    }

}
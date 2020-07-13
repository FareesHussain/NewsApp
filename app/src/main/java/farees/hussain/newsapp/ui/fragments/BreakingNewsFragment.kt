package farees.hussain.newsapp.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import farees.hussain.newsapp.R
import farees.hussain.newsapp.adapter.NewsAdapter
import farees.hussain.newsapp.databinding.FragmentBreakingNewsBinding
import farees.hussain.newsapp.ui.activities.NewsActivity
import farees.hussain.newsapp.ui.activities.NewsViewModel
import farees.hussain.newsapp.util.Resource

class BreakingNewsFragment : Fragment() {

    private lateinit var binding : FragmentBreakingNewsBinding
    private lateinit var viewModel : NewsViewModel
    private lateinit var newsAdapter : NewsAdapter
    private val TAG :String = "Breaking News Fragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_breaking_news,container,false)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        setHasOptionsMenu(true)

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Log.d(TAG,"An Error occured : $message")
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
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
        binding.rvBreakingNews.visibility = View.VISIBLE
    }
    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        binding.rvBreakingNews.visibility = View.GONE
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

    private fun searchNews(query:String){
        viewModel.searchNews(query)
        findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToSearchNewsFragment())
    }

}
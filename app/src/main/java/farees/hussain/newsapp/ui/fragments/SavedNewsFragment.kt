package farees.hussain.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import farees.hussain.newsapp.R
import farees.hussain.newsapp.adapter.NewsAdapter
import farees.hussain.newsapp.databinding.FragmentSavedNewsBinding
import farees.hussain.newsapp.ui.activities.NewsActivity
import farees.hussain.newsapp.ui.activities.NewsViewModel


class SavedNewsFragment : Fragment() {

    private lateinit var binding : FragmentSavedNewsBinding
    private lateinit var viewModel : NewsViewModel
    private lateinit var newsAdapter : NewsAdapter
    private val TAG :String = "Breaking News Fragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_saved_news,container,false)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {article ->
            findNavController().navigate(SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article))
        }


        return binding.root
    }
    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
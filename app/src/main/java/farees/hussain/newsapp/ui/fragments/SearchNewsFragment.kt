package farees.hussain.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import farees.hussain.newsapp.R
import farees.hussain.newsapp.databinding.FragmentSavedNewsBinding
import farees.hussain.newsapp.databinding.FragmentSearchNewsBinding
import farees.hussain.newsapp.ui.activities.NewsActivity
import farees.hussain.newsapp.ui.activities.NewsViewModel

class SearchNewsFragment : Fragment() {

    private lateinit var binding : FragmentSearchNewsBinding
    private lateinit var viewModel : NewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_news,container,false)
        viewModel = (activity as NewsActivity).viewModel


        return binding.root
    }


}
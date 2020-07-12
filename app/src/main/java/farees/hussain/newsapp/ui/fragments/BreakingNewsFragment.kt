package farees.hussain.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import farees.hussain.newsapp.R
import farees.hussain.newsapp.databinding.FragmentBreakingNewsBinding

class BreakingNewsFragment : Fragment() {

    private lateinit var binding : FragmentBreakingNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_breaking_news,container,false)



        return binding.root
    }

}
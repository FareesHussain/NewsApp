package farees.hussain.newsapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import farees.hussain.newsapp.R
import farees.hussain.newsapp.database.ArticleDataBase
import farees.hussain.newsapp.databinding.ActivityNewsBinding
import farees.hussain.newsapp.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewsBinding
    private lateinit var drawerLayout : DrawerLayout
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_news)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_news)
        drawerLayout = binding.drawerLayout

        //navigation drawer
        val NavController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, NavController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, NavController)

        val newsRepository = NewsRepository(ArticleDataBase(this))
        val viewModelFactory = NewsViewModelFactory(application,newsRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(NewsViewModel::class.java)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}
package farees.hussain.newsapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import farees.hussain.newsapp.R
import farees.hussain.newsapp.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewsBinding
    private lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_news)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_news)
        drawerLayout = binding.drawerLayout

        val NavController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, NavController, drawerLayout)

        NavigationUI.setupWithNavController(binding.navView, NavController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}
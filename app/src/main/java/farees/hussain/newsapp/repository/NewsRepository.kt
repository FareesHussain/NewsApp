package farees.hussain.newsapp.repository

import farees.hussain.newsapp.api.RetrofitInstance
import farees.hussain.newsapp.database.ArticleDataBase

class NewsRepository(
    val db : ArticleDataBase
) {
    suspend fun getBreakingNews(countryCode:String, pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery : String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)
}
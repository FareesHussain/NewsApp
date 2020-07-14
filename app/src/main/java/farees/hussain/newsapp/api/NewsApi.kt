package farees.hussain.newsapp.api

import farees.hussain.newsapp.models.NewsResponse
import farees.hussain.newsapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode :String = "in",
        @Query("page")
        pageNumber : Int = 1,
//        @Query("category")
//        type :String = "entertainment",
        @Query("pageSize")
        pageSize :Int = 20,
        @Query("apiKey")
        apikey : String = Constants.API_KEY
    ):Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun searchForNews(
        @Query("q")
        searchQuery :String = "",
        @Query("page")
        pageNumber : Int = 1,
//        @Query("country")
//        countryCode :String = "in",
//        @Query("category")
//        type :String = "entertainment",
        @Query("pageSize")
        pageSize :Int = 20,
        @Query("apiKey")
        apikey : String = Constants.API_KEY
    ):Response<NewsResponse>
}
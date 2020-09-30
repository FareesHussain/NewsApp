package farees.hussain.newsapp.ui.activities

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import farees.hussain.newsapp.NewsApplication
import farees.hussain.newsapp.models.NewsArticle
import farees.hussain.newsapp.models.NewsResponse
import farees.hussain.newsapp.repository.NewsRepository
import farees.hussain.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel (
        app:Application,
    val newsRepository : NewsRepository
) : AndroidViewModel(app){

    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews : LiveData<Resource<NewsResponse>>
        get() = _breakingNews
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse ?= null
    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()
    val searchNews : LiveData<Resource<NewsResponse>>
        get() = _searchNews
    var searchNewsPage = 1
    var searchNewsResponse : NewsResponse ?= null
    init {
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        safeBreakingNewsCall(countryCode)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                }
                else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newsArticles = resultResponse.articles
                    oldArticles!!.addAll(newsArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchNews(searchQuery:String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        safeSearchNewsCall(searchQuery)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                }
                else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle (article: NewsArticle) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun getSavedNews() =
        newsRepository.getSavedNews()
    fun deleteSavedNews(article: NewsArticle) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        _breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
                _breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                _breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> _breakingNews.postValue(Resource.Error("network failure"))
                else -> _breakingNews.postValue(Resource.Error("Convertion Error"))
            }
        }
    }
    private suspend fun safeSearchNewsCall(searchQuery: String){
        _searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = newsRepository.searchNews(searchQuery,searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                _searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> _searchNews.postValue(Resource.Error("network failure"))
                else -> _searchNews.postValue(Resource.Error("Convertion Error"))
            }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}
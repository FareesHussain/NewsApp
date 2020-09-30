package farees.hussain.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import farees.hussain.newsapp.models.NewsArticle

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: NewsArticle):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<NewsArticle>>

    @Delete
    suspend fun deleteArticle(article: NewsArticle)

}
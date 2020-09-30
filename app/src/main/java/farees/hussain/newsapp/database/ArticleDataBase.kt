package farees.hussain.newsapp.database

import android.content.Context
import androidx.room.*
import farees.hussain.newsapp.models.NewsArticle


@Database(
    entities = [NewsArticle::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDataBase : RoomDatabase() {
    abstract fun getArticleDao() : ArticleDao

    companion object{
        @Volatile
        private var instance : ArticleDataBase?=null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticleDataBase::class.java,
            "article_db.db"
        ).build()
    }
}
package com.example.wantednews.room

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.wantednews.data.TopHeadlinesData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Database(entities = [SaveArticles::class], version = 1, exportSchema = false)
@TypeConverters(ArticleTypeConverter::class)
abstract class SaveArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): SaveArticleDao

    companion object {
        private var instance: SaveArticleDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SaveArticleDatabase? = if (instance == null) {
            synchronized(SaveArticleDatabase::class) {
                instance = Room.databaseBuilder(context.applicationContext, SaveArticleDatabase::class.java, "saveArticles").build()

                instance
            }
        } else {
            instance
        }
    }
}

@Dao
interface SaveArticleDao {
    @Insert
    fun insert(vararg saveArticle: SaveArticles)

    @Query("DELETE FROM saveArticles WHERE article = :article")
    fun delete(article: TopHeadlinesData.Article)

    @Query("SELECT EXISTS(SELECT * FROM saveArticles WHERE article = :article)")
    fun isSaved(article: TopHeadlinesData.Article): Boolean

    @Query("SELECT * FROM saveArticles")
    fun getSavedList(): LiveData<List<SaveArticles>>
}

@Entity(tableName = "saveArticles")
@Parcelize
data class SaveArticles(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val article: TopHeadlinesData.Article
) : Parcelable

class ArticleTypeConverter {
    @TypeConverter
    fun articleToJson(value: TopHeadlinesData.Article): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToArticle(value: String): TopHeadlinesData.Article = Gson().fromJson(value, TopHeadlinesData.Article::class.java)
}
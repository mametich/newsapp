package non.mametich.newsapp.data.api.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import non.mametich.newsapp.models.Article


@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles")
    fun getAllArticle(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)
}
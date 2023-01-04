package non.mametich.newsapp.data.api.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import non.mametich.newsapp.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = true)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

}
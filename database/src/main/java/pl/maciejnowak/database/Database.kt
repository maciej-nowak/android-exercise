package pl.maciejnowak.database

import android.content.Context
import androidx.room.Room

object Database {

    private var appDatabase: AppDatabase? = null

    //throws NPE if init hasn't called - need to fix this
    val wikiDao: WikiDao by lazy { appDatabase!!.wikiDao() }
    val articleDao: ArticleDao by lazy { appDatabase!!.articleDao() }

    //must force to init
    fun init(context: Context) {
        if(appDatabase == null) {
            appDatabase = Room
                .databaseBuilder(context, AppDatabase::class.java, "AppDatabase")
                .build()
        }
    }
}
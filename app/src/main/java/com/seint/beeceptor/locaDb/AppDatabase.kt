package com.seint.beeceptor.locaDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.seint.beeceptor.model.Article

@Database(entities = arrayOf(Article::class),version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun articleDao() : ArticleDao

    companion object{
        //singleton to prevent multiple instances of database opening
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDataBase(context: Context) : AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return  tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,"appDataBase").build()
                INSTANCE = instance
                return  instance
            }
        }
    }
}

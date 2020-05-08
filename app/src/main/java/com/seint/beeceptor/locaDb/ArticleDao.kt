package com.seint.beeceptor.locaDb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seint.beeceptor.model.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM Article")
    fun getAllArticle() : LiveData<List<Article>>

    @Query("SELECT * FROM Article WHERE id =:articleID")
    fun getArticleByID(articleID :Int) : LiveData<Article>


    @Query("DELETE FROM Article")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles( articleList: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle( article: Article)

    @Update
    fun update(article: Article)

    @Delete
    fun delete(article: Article)

}
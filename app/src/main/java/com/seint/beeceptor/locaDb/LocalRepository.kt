package com.seint.beeceptor.locaDb

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.seint.beeceptor.model.Article



class LocalRepository (private val articleDao: ArticleDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    var articleList: LiveData<List<Article>> =articleDao.getAllArticle()

    fun insertArticles(articleList: List<Article>){

        InsertAlLAsyncTask(articleDao).execute(*articleList.toTypedArray())
       // articleDao.insertArticles(articleList)
    }

    fun getArticles() :LiveData<List<Article>>{
        return articleDao.getAllArticle()
    }
    fun getArticle(id:Int) :LiveData<Article>{
        return articleDao.getArticleByID(id)
    }
    fun clearData() {
        DeleteAllAsyncTask(articleDao).execute()
    }
    fun updateArticle(article : Article) {
        UpdateAsyncTask(articleDao).execute(article)
    }
    companion object {
        private class InsertAlLAsyncTask(private val articleDao: ArticleDao) : AsyncTask<Article, Void, Void>() {

            override fun doInBackground(vararg articles: Article): Void? {
                articleDao.insertArticles(articles.toList())
                return null
            }

        }
        private class InsertAsyncTask(private val articleDao: ArticleDao) : AsyncTask<Article, Void, Void>() {

            override fun doInBackground(vararg articles: Article): Void? {
                articleDao.insertArticle(articles[0])
                return null
            }
        }

        private class DeleteAsyncTask(private val articleDao: ArticleDao) : AsyncTask<Article, Void, Void>() {

            override fun doInBackground(vararg appointments: Article): Void? {
                articleDao.delete(appointments[0])
                return null
            }
        }
        private class UpdateAsyncTask(private val articleDao: ArticleDao) : AsyncTask<Article, Void, Void>() {

            override fun doInBackground(vararg appointments: Article): Void? {
                articleDao.update(appointments[0])
                return null
            }
        }
        private class DeleteAllAsyncTask(private val articleDao: ArticleDao) : AsyncTask<Article, Void, Void>() {

            override fun doInBackground(vararg appointments: Article): Void? {
                articleDao.nukeTable()
                return null
            }
        }
    }
}
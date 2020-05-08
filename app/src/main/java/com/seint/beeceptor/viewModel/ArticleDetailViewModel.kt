package com.seint.beeceptor.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seint.beeceptor.locaDb.AppDatabase
import com.seint.beeceptor.locaDb.LocalRepository
import com.seint.beeceptor.model.Article
import com.seint.beeceptor.retrofit.APIResponse
import com.seint.beeceptor.retrofit.Repository

class ArticleDetailViewModel ( application: Application, article: Article) : AndroidViewModel(application),
    APIResponse<Article> {

    val localRepository : LocalRepository
    private var article : LiveData<Article> ;
    fun getErrorUpdates(): LiveData<Map<Int,String>> {
        return Repository.instance.getErrorData()
    }

    init {

        val articleDao = AppDatabase.getDataBase(application).articleDao()
        localRepository = LocalRepository(articleDao)
        this.article = localRepository.getArticle(article.id)

        if(article.text.isNullOrEmpty()){
            //this.article = loadJSONFromAsset(application,article)
             Repository.instance.getArticle(article,this)
        }

    }
    fun updateArticle(article: Article){
        localRepository.updateArticle(article)
    }
    fun getArticle() : LiveData<Article>{
        return  article
    }
    //this is for my internal testing
    fun loadJSONFromAsset(application: Application, article: Article): MutableLiveData<Article> {
        var json: String? = null
        val liveData = MutableLiveData<Article>()
        liveData.value =article
        val inputStream = application.assets.open("article_detail.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charsets.UTF_8)
        val turns = Gson().fromJson<Article>(json, Article::class.java)
        liveData.value?.text = turns.text

        val newArticle = liveData.value
        if(newArticle != null ){
            localRepository.updateArticle(newArticle)
            this.article = localRepository.getArticle(newArticle.id)
        }
        return liveData
    }

    override fun onSuccess(data: LiveData<Article>) {
        val newArticle = data.value
        if(newArticle != null ){
            localRepository.updateArticle(newArticle)
            this.article = localRepository.getArticle(newArticle.id)
        }
    }

}
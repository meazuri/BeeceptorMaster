package com.seint.beeceptor.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seint.beeceptor.model.Article
import com.seint.beeceptor.retrofit.Repository

class ArticleDetailViewModel ( application: Application, article: Article) : AndroidViewModel(application){

    private lateinit var article : MutableLiveData<Article> ;
    fun getErrorUpdates(): LiveData<Map<Int,String>> {
        return Repository.instance.getErrorData()
    }

    init {
        this.article = Repository.instance.getArticle(article)
        //this.article = loadJSONFromAsset(application,article)

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
        return liveData
    }

}
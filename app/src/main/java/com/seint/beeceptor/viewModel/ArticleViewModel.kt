package com.seint.beeceptor.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seint.beeceptor.model.Article
import com.seint.beeceptor.retrofit.Repository


class ArticleViewModel( application: Application) : AndroidViewModel(application) {


    private var articleListObservable: LiveData<List<Article>> = MutableLiveData<List<Article>>()
    fun getErrorUpdates(): LiveData<Map<Int,String>> {
        return Repository.instance.getErrorData()
    }

    init {
        articleListObservable = Repository.instance.getArticleList()
        //articleListObservable = loadJSONFromAsset(application)
    }

    fun getArticleListObservable(): LiveData<List<Article>> {
        return articleListObservable
    }

    //this is for my internal testing
    fun loadJSONFromAsset(application: Application): MutableLiveData<List<Article>> {
        var json: String? = null

        val inputStream = application.assets.open("article.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charsets.UTF_8)
        val turnsType = object : TypeToken<List<Article>>() {}.type
        val turns = Gson().fromJson<List<Article>>(json, turnsType)

        val liveData = MutableLiveData<List<Article>>()
        liveData.value  = turns
        return liveData
    }

}
package com.seint.beeceptor.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seint.beeceptor.model.Article
import com.seint.beeceptor.retrofit.Repository
import com.seint.beeceptor.locaDb.AppDatabase
import com.seint.beeceptor.locaDb.LocalRepository
import com.seint.beeceptor.locaDb.LocalSharePreference
import com.seint.beeceptor.retrofit.APIResponse
import java.util.*


class ArticleViewModel( application: Application) : AndroidViewModel(application),
    APIResponse<List<Article>> {

    val localRepository : LocalRepository
    private var articleListObservable: LiveData<List<Article>> = MutableLiveData<List<Article>>()
    fun getErrorUpdates(): LiveData<Map<Int,String>> {
        return Repository.instance.getErrorData()
    }

    init {
        val articleDao = AppDatabase.getDataBase(application).articleDao()
        localRepository = LocalRepository(articleDao)
        //articleListObservable = Repository.instance.getArticleList()
        articleListObservable = localRepository.articleList

        if(isTimeToUpdate()){
           // Repository.instance.getArticleList(this)
            Log.i("Times Up","Time to Update")
            //for local offline test
            val newArticleList = loadJSONFromAsset(application).value
            if(!newArticleList.isNullOrEmpty()){
                localRepository.clearData()
                localRepository.insertArticles(newArticleList)
                articleListObservable = localRepository.articleList
            }

        }

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
    fun isTimeToUpdate() :Boolean {
        val cal: Calendar = Calendar.getInstance()
        cal.clear(Calendar.HOUR)
        cal.clear(Calendar.HOUR_OF_DAY)
        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        val now: Long = cal.getTimeInMillis()
        val lastCheckedMillis = LocalSharePreference.getLastUpdatedTime(getApplication())
        val diffMillis: Long = now - lastCheckedMillis
        if (diffMillis >= 3600000 * 24){
            LocalSharePreference.saveLastUpdateTime(getApplication(),now)
            return true
        }else{
            return false
        }
    }

    override fun onSuccess(data: LiveData<List<Article>>) {
        val newArticleList = data.value
        if(!newArticleList.isNullOrEmpty()){
            localRepository.clearData()
            localRepository.insertArticles(newArticleList)
            articleListObservable = localRepository.articleList
        }
    }
}
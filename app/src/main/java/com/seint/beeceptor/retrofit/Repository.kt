package com.seint.beeceptor.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.seint.beeceptor.model.Article
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream


class Repository private constructor(){

        private val retrofitService: RetrofitService
        val HTTPS_API_URL = "https://task.free.beeceptor.com"
        private val mError: MutableLiveData<String> = MutableLiveData()

    init {
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder()
            // add your other interceptors …
            // add logging as last interceptor
            httpClient.addInterceptor(logging)
            val retrofit = Retrofit.Builder()
                .baseUrl(HTTPS_API_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofitService = retrofit.create(RetrofitService::class.java!!)
        }

    companion object {
        private var projectRepository: Repository? = null

        val instance: Repository
            @Synchronized get() {
                if (projectRepository == null) {
                    if (projectRepository == null) {
                        projectRepository = Repository()
                    }
                }
                return projectRepository as Repository
            }
    }

    fun getArticleList(): MutableLiveData<List<Article>> {
        var data = MutableLiveData<List<Article>>()

        retrofitService.articleList().enqueue(object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {

                if(response.code() == 200 ) {
                    data.value = response.body()
                }else{
                    mError.value = response.errorBody()?.charStream()?.readText()
                }
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                data.setValue(null)
                mError.value = t.message
            }
        })

        return data
    }
    fun getErrorData(): LiveData<String> {
        return mError
    }



}
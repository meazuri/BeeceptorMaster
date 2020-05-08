package com.seint.beeceptor.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.seint.beeceptor.model.Article
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Repository private constructor(){

        private val retrofitService: RetrofitService
        val HTTPS_API_URL = "https://task.free.beeceptor.com"
        private val mError: MutableLiveData<Map<Int,String>> = MutableLiveData()

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
                    val message = response.errorBody()?.charStream()?.readText().toString()
                    mError.value = mapOf(Pair(1,message))
                }
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                data.setValue(null)
                val message = t.message.toString().let {
                    mError.value = null
                } as String
                mError.value = mapOf(Pair(1,message))

            }
        })

        return data
    }
    fun getErrorData(): LiveData<Map<Int,String>> {
        return mError
    }

    fun getArticle(article: Article): MutableLiveData<Article> {
        var data = MutableLiveData<Article>()
        data.value = article
        retrofitService.getArticle(article.id.toString()).enqueue(object : Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {

                if(response.code() == 200 ) {
                    var text  = response.body()?.text
                    data.value?.text = text?:""
                }else{

                    var message :String = response.errorBody()?.charStream()?.readText().toString()
                    mError.value = mapOf(Pair(2,message))
                }
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                data.setValue(null)
                var message = t.message.toString().let {
                    mError.value = null
                } as String
                mError.value = mapOf(Pair(2,message))
            }
        })
        return  data
    }


}
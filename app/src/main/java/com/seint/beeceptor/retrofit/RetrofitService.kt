package com.seint.beeceptor.retrofit

import com.seint.beeceptor.model.Article
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RetrofitService {

    @GET("/article")
    fun articleList() : Call<List<Article>>

}
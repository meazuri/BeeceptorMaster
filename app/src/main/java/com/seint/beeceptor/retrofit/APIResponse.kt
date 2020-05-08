package com.seint.beeceptor.retrofit

import androidx.lifecycle.LiveData


interface APIResponse<T> {

    fun onSuccess( data: LiveData<T>)
}
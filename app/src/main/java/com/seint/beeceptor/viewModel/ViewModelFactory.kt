package com.seint.beeceptor.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import com.seint.beeceptor.model.Article


class ArticleViewModelFactory( application: Application, param: Article) :
    ViewModelProvider.Factory {
    private val mParam: Article
    private val application :Application
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleDetailViewModel(application, mParam) as T
    }

    init {
        mParam = param
        this.application = application
    }
}
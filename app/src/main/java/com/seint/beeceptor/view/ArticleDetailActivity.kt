package com.seint.beeceptor.view

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.seint.beeceptor.R
import com.seint.beeceptor.model.Article
import com.seint.beeceptor.viewModel.ArticleViewModelFactory
import com.seint.beeceptor.viewModel.ArticleDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_article_detail.*
import kotlinx.android.synthetic.main.activity_article_detail.progress_circular
import kotlinx.android.synthetic.main.activity_main.*

class ArticleDetailActivity : AppCompatActivity() {

    lateinit var articleDetailViewModel: ArticleDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        var article = intent.getSerializableExtra("ARTICLE") as? Article
        if( article != null ) {
            supportActionBar?.title = article.title
            progress_circular.visibility = View.VISIBLE
            Picasso.get().load(article.avatar).placeholder(R.drawable.loadinganimation).error(R.drawable.error).into(imgAvator)

            articleDetailViewModel = ViewModelProviders.of(
                this,
                ArticleViewModelFactory(application,article)
            )
                .get<ArticleDetailViewModel>(
                    ArticleDetailViewModel::class.java
                )
            articleDetailViewModel.getArticle().observe(this, Observer {
                val handler = Handler()
                handler.postDelayed(Runnable {
                    if (it != null) {
                        tvArticle.text = it.text
                    }
                    progress_circular.visibility = View.GONE
                }, 3000     )


            })

            articleDetailViewModel.getErrorUpdates().observe(this, Observer {

                if (!it.isNullOrEmpty()){
                    if(it.containsKey(2)){
                        showErrorDialog(this,"Error",message = it.get(2) as String)
                    }
                }
                progress_circular.visibility = View.GONE
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu)
    }
}

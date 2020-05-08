package com.seint.beeceptor.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.os.Handler
import android.text.method.KeyListener
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.seint.beeceptor.R
import com.seint.beeceptor.model.Article
import com.seint.beeceptor.viewModel.ArticleDetailViewModel
import com.seint.beeceptor.viewModel.ArticleViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_article_detail.*


class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var menu: Menu
    lateinit var articleDetailViewModel: ArticleDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        makeItBeautiful()

        var article = intent.getSerializableExtra("ARTICLE") as? Article
        if( article != null ) {
            bindViewModel(article)
        }
    }
    fun bindViewModel(article :Article){
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
                    tvArticle.setText(it.text)
                }
                progress_circular.visibility = View.GONE
                menu.findItem(R.id.edit).setEnabled(true)
            }, 2000     )


        })

        articleDetailViewModel.getErrorUpdates().observe(this, Observer {

            if (!it.isNullOrEmpty()){
                if(it.containsKey(2)){
                    menu.findItem(R.id.edit).setEnabled(false)
                    showErrorDialog(this,"Error",message = it.get(2) as String)
                }
            }
            progress_circular.visibility = View.GONE
        })
    }
    fun makeItBeautiful(){
        tvArticle.setTag(tvArticle.getKeyListener());
        tvArticle.setKeyListener(null);
        tvArticle.setBackgroundColor(resources.getColor(R.color.colorTransparent))
        btnSave.visibility =View.GONE
        btnSave.setOnClickListener {
            var article = articleDetailViewModel.getArticle().value
            article?.text = tvArticle.text.toString()
            var resultIntent = Intent()
            resultIntent.putExtra("ARTICLE",article)
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.edit) {
            item.setVisible(false)
            menu.findItem(R.id.cancel).setVisible(true)
            tvArticle.setKeyListener(tvArticle.getTag() as KeyListener)
            tvArticle.setBackgroundColor(resources.getColor(R.color.colorGrey))
            tvArticle.requestFocus()
            btnSave.visibility =View.VISIBLE

        }else if (id == R.id.cancel){
            menu.findItem(R.id.edit).setVisible(false)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

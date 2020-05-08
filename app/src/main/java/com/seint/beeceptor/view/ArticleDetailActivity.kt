package com.seint.beeceptor.view

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
import kotlinx.android.synthetic.main.activity_article_detail.progress_circular
import kotlinx.android.synthetic.main.activity_main.*


class ArticleDetailActivity : AppCompatActivity() {

    private  var menu: Menu? = null
    lateinit var articleDetailViewModel: ArticleDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        progress_circular.visibility = View.VISIBLE
        makeItBeautiful()

        var article = intent.getSerializableExtra("ARTICLE") as? Article
        if( article != null ) {
            bindViewModel(article)
        }
    }
    fun bindViewModel(article :Article){
        supportActionBar?.title = article.title


        Picasso.get().load(article.avatar).placeholder(R.drawable.loadinganimation).error(R.drawable.error).into(imgAvator)

        articleDetailViewModel = ViewModelProviders.of(
            this,
            ArticleViewModelFactory(application,article)
        )
            .get<ArticleDetailViewModel>(
                ArticleDetailViewModel::class.java
            )
        articleDetailViewModel.getArticle().observe(this, Observer {
            if (it != null) {
                tvArticle.setText(it.text)
            }
            val handler = Handler()
            handler.postDelayed(Runnable {
                progress_circular.visibility = View.GONE
            }, 3000     )

            progress_circular.visibility = View.GONE
            menu?.findItem(R.id.edit)?.setEnabled(true)

        })

        articleDetailViewModel.getErrorUpdates().observe(this, Observer {

            if (!it.isNullOrEmpty()){
                if(it.containsKey(2)){
                    menu?.findItem(R.id.edit)?.setEnabled(false)
                    showDialogWindow(this,"Error",message = it.get(2) as String)
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
            val article = articleDetailViewModel.getArticle().value
            article?.text = tvArticle.text.toString()
            article?.let { it1 -> articleDetailViewModel.updateArticle(it1) }
            finish()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu)
        if (menu != null) {
            this.menu = menu
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.edit) {
            item.setVisible(false)
            menu?.findItem(R.id.cancel)?.setVisible(true)
            tvArticle.setKeyListener(tvArticle.getTag() as KeyListener)
            tvArticle.setBackgroundColor(resources.getColor(R.color.colorGrey))
            tvArticle.requestFocus()
            btnSave.visibility =View.VISIBLE

        }else if (id == R.id.cancel){
            menu?.findItem(R.id.edit)?.setVisible(false)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

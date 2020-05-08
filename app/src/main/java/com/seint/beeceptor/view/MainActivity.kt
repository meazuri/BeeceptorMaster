package com.seint.beeceptor.view

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seint.beeceptor.R
import com.seint.beeceptor.viewModel.ArticleViewModel
import kotlinx.android.synthetic.main.activity_article_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progress_circular

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: ArticleListAdapter
    lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = ArticleListAdapter(this)
        val lm = LinearLayoutManager(this)
        lm.orientation = RecyclerView.VERTICAL

        recyclerView.layoutManager = lm
        recyclerView.adapter = mAdapter
        recyclerView.isNestedScrollingEnabled = false

        progress_circular.visibility = View.VISIBLE
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

        articleViewModel.getArticleListObservable().observe(this, Observer {


                val handler = Handler()
                handler.postDelayed(Runnable {
                    if(it != null) {
                        mAdapter.setArticleData(it)
                        mAdapter.notifyDataSetChanged()

                    }

                    progress_circular.visibility = View.GONE
                }, 5000     )


        })

        articleViewModel.getErrorUpdates().observe(this, Observer {

             if (!it.isNullOrEmpty()){
                 if(it.containsKey(1)){
                     showErrorDialog(this,"Error",message = it.get(1) as String)

                 }

             }
            progress_circular.visibility = View.GONE
        })
    }
}

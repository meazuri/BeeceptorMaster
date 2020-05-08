package com.seint.beeceptor.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seint.beeceptor.R
import com.seint.beeceptor.formatDate
import com.seint.beeceptor.model.Article
import com.squareup.picasso.Picasso
import java.util.*


class ArticleListAdapter(private val context: Context) : RecyclerView.Adapter<ArticleListAdapter.MyViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)

    private var articleList: List<Article> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = layoutInflater.inflate(R.layout.article_list_item, parent, false)
        return MyViewHolder(itemView)
    }
    override fun getItemCount() = articleList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = articleList[position]
        holder.tvTitle?.text = article.title
        holder.tvDesp?.text = article.short_description

        holder.tvDate?.text =  Date (article.last_update.toLong() * 1000) .formatDate("dd MM yyyy")
        holder.dataPosition = position
        Picasso.get().load(article.avatar).placeholder(R.drawable.loadinganimation).error(R.drawable.error).into(holder.imgAvator)

    }

    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val tvTitle = itemView?.findViewById<TextView?>(R.id.tvTitle)
        var tvDesp = itemView?.findViewById<TextView?>(R.id.tvDescription)
        var tvDate = itemView?.findViewById<TextView>(R.id.tvDate)
        var imgAvator = itemView?.findViewById<ImageView>(R.id.imgAvator)
        var dataPosition = 0
        init {
            itemView?.setOnClickListener {
                var intent = Intent(context,ArticleDetailActivity::class.java)
                intent.putExtra("ARTICLE",articleList.get(dataPosition))
                (context as MainActivity).startActivityForResult(intent ,1)
            }
        }
    }
    fun setArticleData(articlelist: List<Article>){
        this.articleList = articlelist
        notifyDataSetChanged()
    }
}
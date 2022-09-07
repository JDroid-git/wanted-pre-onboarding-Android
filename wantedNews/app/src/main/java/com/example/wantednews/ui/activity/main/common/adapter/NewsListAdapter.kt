package com.example.wantednews.ui.activity.main.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wantednews.R
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.databinding.ViewTopNewsListItemBinding
import com.example.wantednews.util.DateUtil

class NewsListAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    private val newsList = ArrayList<TopHeadlinesData.Article>()

    interface OnClickListener {
        fun onItemClickListener(article: TopHeadlinesData.Article)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNews(list: ArrayList<TopHeadlinesData.Article>) {
        newsList.clear()
        newsList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ViewTopNewsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = newsList.size

    inner class ViewHolder(private val binding: ViewTopNewsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            newsList[adapterPosition].run {
                Glide.with(itemView.context)
                    .load(urlToImage)
                    .error(ContextCompat.getDrawable(itemView.context, R.drawable.image_none))
                    .centerCrop()
                    .into(binding.imgBanner)

                binding.txtTitle.text = title
                binding.txtWriter.text = author
                binding.txtDate.text = DateUtil.compareDate(publishedAt ?: "")

                itemView.setOnClickListener { onClickListener.onItemClickListener(this) }
            }
        }
    }
}
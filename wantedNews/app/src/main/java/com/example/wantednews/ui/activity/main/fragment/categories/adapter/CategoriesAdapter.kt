package com.example.wantednews.ui.activity.main.fragment.categories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wantednews.databinding.ViewCategoryListItemBinding

class CategoriesAdapter(private val categoryList: ArrayList<Pair<String, Int>>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onItemClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ViewCategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = categoryList.size

    inner class ViewHolder(private val binding: ViewCategoryListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.imgCategory.setImageDrawable(ContextCompat.getDrawable(itemView.context, categoryList[adapterPosition].second))
            binding.txtCategory.text = categoryList[adapterPosition].first

            itemView.setOnClickListener { onClickListener.onItemClickListener(adapterPosition) }
        }
    }
}
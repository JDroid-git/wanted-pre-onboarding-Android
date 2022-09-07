package com.example.wantednews.ui.activity.main.common.fragment.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.databinding.FragmentNewsDetailBinding
import com.example.wantednews.room.SaveArticleDao
import com.example.wantednews.room.SaveArticleDatabase
import com.example.wantednews.room.SaveArticles
import com.example.wantednews.ui.activity.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsDetailFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding

    private var newsInfo: TopHeadlinesData.Article? = null
    private var articleDao: SaveArticleDao? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.chkSave?.id -> {
                CoroutineScope(Dispatchers.IO).launch {
                    newsInfo?.let {
                        if (binding?.chkSave?.isChecked == true) {
                            articleDao?.insert(SaveArticles(article = it))
                        } else {
                            articleDao?.delete(it)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)

        initData()
        initLayout()
        initListener()

        return binding?.root
    }

    private fun initData() {
        arguments?.getParcelable<TopHeadlinesData.Article>(Constants.BundleTag.TAG_NEWS_INFO)?.let {
            newsInfo = it
        }
        articleDao = SaveArticleDatabase.getInstance(requireContext().applicationContext)?.articleDao()
    }

    private fun initLayout() {
        newsInfo?.let {
            (requireActivity() as MainActivity).supportActionBar?.apply {
                title = it.title
                setDisplayHomeAsUpEnabled(true)
            }

            CoroutineScope(Dispatchers.IO).launch {
                val isSaved = articleDao?.isSaved(it)

                withContext(Dispatchers.Main) {
                    binding?.chkSave?.isChecked = isSaved == true
                }
            }

            binding?.imgBanner?.let { imgBanner ->
                Glide.with(requireContext())
                    .load(it.urlToImage)
                    .into(imgBanner)
            }
            binding?.txtTitle?.text = it.title
            binding?.txtWriter?.text = it.author
            binding?.txtDate?.text = it.publishedAt
            binding?.txtContent?.text = it.content
        }
    }

    private fun initListener() {
        binding?.chkSave?.setOnClickListener(this)
    }
}
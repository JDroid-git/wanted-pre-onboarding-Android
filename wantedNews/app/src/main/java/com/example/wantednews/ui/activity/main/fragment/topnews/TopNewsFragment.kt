package com.example.wantednews.ui.activity.main.fragment.topnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wantednews.R
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.databinding.FragmentNewsListBinding
import com.example.wantednews.ui.activity.main.common.adapter.NewsListAdapter

class TopNewsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private var topNewsListAdapter: NewsListAdapter? = null
    private var topNewsViewModel: TopNewsViewModel? = null

    private val onClickListener = object : NewsListAdapter.OnClickListener {
        override fun onItemClickListener(article: TopHeadlinesData.Article) {
            val bundle = Bundle().apply { putParcelable(Constants.BundleTag.TAG_NEWS_INFO, article) }
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_news_detail_fragment, bundle)
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!recyclerView.canScrollVertically(1)) {
                topNewsViewModel?.requestNewsList(true)
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    override fun onRefresh() {
        topNewsViewModel?.requestNewsList(false)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)

        initData()
        initLayout()
        initListener()
        initObserve()

        requestNewsList()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {
        topNewsViewModel = ViewModelProvider(this)[TopNewsViewModel(requireActivity().application)::class.java]
        if (topNewsViewModel?.newsList?.value.isNullOrEmpty()) {
            topNewsViewModel?.requestNewsList(false)
        }
    }

    private fun initLayout() {
        topNewsListAdapter = NewsListAdapter(onClickListener)
        binding.newsList.adapter = topNewsListAdapter
    }

    private fun initListener() {
        binding.newsList.addOnScrollListener(onScrollListener)
        binding.swipeLayout.setOnRefreshListener(this)
    }

    private fun initObserve() {
        topNewsViewModel?.isLoading?.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it == true
        }
        topNewsViewModel?.newsList?.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = false
            topNewsListAdapter?.updateNews(it)
        }
    }

    private fun requestNewsList() {
        binding.progressBar.isVisible = true
    }
}
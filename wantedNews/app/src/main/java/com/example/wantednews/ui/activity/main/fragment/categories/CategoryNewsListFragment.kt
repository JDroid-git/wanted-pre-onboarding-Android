package com.example.wantednews.ui.activity.main.fragment.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wantednews.R
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.databinding.FragmentNewsListBinding
import com.example.wantednews.ui.activity.main.MainActivity
import com.example.wantednews.ui.activity.main.common.adapter.NewsListAdapter

class CategoryNewsListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private var categoryNewsListAdapter: NewsListAdapter? = null
    private var category = ""

    private var categoryViewModel: CategoryViewModel? = null

    private val onClickListener = object : NewsListAdapter.OnClickListener {
        override fun onItemClickListener(article: TopHeadlinesData.Article) {
            val bundle = Bundle().apply { putParcelable(Constants.BundleTag.TAG_NEWS_INFO, article) }
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_news_detail_fragment, bundle)
        }
    }

    private val onScrolled = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!recyclerView.canScrollVertically(1)) {
                categoryViewModel?.requestNewsList(true, category)
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    override fun onRefresh() {
        categoryViewModel?.requestNewsList(false, category)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)

        initData()
        initLayout()
        initListener()
        initObserve()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel(requireActivity().application)::class.java]
        category = arguments?.getString(Constants.BundleTag.TAG_CATEGORY) ?: ""

        if (categoryViewModel?.categoryNewsList?.value.isNullOrEmpty()) {
            categoryViewModel?.requestNewsList(false, category)
        }
    }

    private fun initLayout() {
        (requireActivity() as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.category_news_list_title, "${category[0].uppercase()}${category.slice(1..category.lastIndex)}")
        }
        categoryNewsListAdapter = NewsListAdapter(onClickListener)
        binding.newsList.adapter = categoryNewsListAdapter
    }

    private fun initListener() {
        binding.swipeLayout.setOnRefreshListener(this)
        binding.newsList.addOnScrollListener(onScrolled)
    }

    private fun initObserve() {
        categoryViewModel?.isLoading?.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = it == true
        }
        categoryViewModel?.categoryNewsList?.observe(viewLifecycleOwner) {
            categoryNewsListAdapter?.updateNews(it)
        }
    }
}
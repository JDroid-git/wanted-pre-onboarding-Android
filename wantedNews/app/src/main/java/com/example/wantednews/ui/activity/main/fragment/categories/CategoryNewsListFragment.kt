package com.example.wantednews.ui.activity.main.fragment.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wantednews.R
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.databinding.FragmentNewsListBinding
import com.example.wantednews.server.ServerService
import com.example.wantednews.server.ServerCallback
import com.example.wantednews.ui.activity.main.MainActivity
import com.example.wantednews.ui.activity.main.common.adapter.NewsListAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class CategoryNewsListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private val newsList: ArrayList<TopHeadlinesData.Article> = arrayListOf()

    private var categoryNewsListAdapter: NewsListAdapter? = null
    private var category = ""
    private var page = 1

    private val serverCallback = object : ServerCallback {
        override fun <Any> onSuccessResponse(call: Call<Any>, response: Response<Any>) {
            binding.progressBar.isVisible = false
            binding.swipeLayout.isRefreshing = false
            val data = response.body() as TopHeadlinesData.TopHeadlines

            setNews(data.articles)
        }

        override fun <Any> onFailResponse(call: Call<Any>, responseCode: Int?, response: ResponseBody?, t: Throwable?) {
            binding.swipeLayout.isRefreshing = false
            binding.progressBar.isVisible = false
        }
    }

    private val onClickListener = object : NewsListAdapter.OnClickListener {
        override fun onItemClickListener(article: TopHeadlinesData.Article) {
            val bundle = Bundle().apply { putParcelable(Constants.BundleTag.TAG_NEWS_INFO, article) }
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_news_detail_fragment, bundle)
        }
    }

    private val onScrolled = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!recyclerView.canScrollVertically(1)) {
                if (page * 20 == newsList.size) {
                    page += 1
                    requestNewsList(page)
                }
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    override fun onRefresh() {
        page = 1
        requestNewsList(page)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)

        initData()
        initLayout()
        initListener()

        page = 1
        requestNewsList(page)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {
        category = arguments?.getString(Constants.BundleTag.TAG_CATEGORY) ?: ""
    }

    private fun initLayout() {
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        categoryNewsListAdapter = NewsListAdapter(onClickListener)
        binding.newsList.adapter = categoryNewsListAdapter
    }

    private fun initListener() {
        binding.swipeLayout.setOnRefreshListener(this)
        binding.newsList.addOnScrollListener(onScrolled)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setNews(data: ArrayList<TopHeadlinesData.Article>?) {
        data?.let {
            if (page * 20 > newsList.size) {
                val oldSize = newsList.size
                newsList.addAll(it)
                categoryNewsListAdapter?.notifyItemRangeInserted(oldSize, 20)
            } else {
                newsList.clear()
                newsList.addAll(it)
                categoryNewsListAdapter?.notifyDataSetChanged()
            }
        }

    }

    private fun requestNewsList(page: Int) {
        binding.progressBar.isVisible = true
        Log.d("pages", "$page")
//        ServerService.getTopHeadlines(serverCallback, Constants.Countries.COUNTRY_KR, category, null, null, page)
    }
}
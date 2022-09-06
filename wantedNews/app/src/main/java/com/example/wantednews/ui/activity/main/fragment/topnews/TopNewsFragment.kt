package com.example.wantednews.ui.activity.main.fragment.topnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.wantednews.R
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.databinding.FragmentNewsListBinding
import com.example.wantednews.server.ServerApi
import com.example.wantednews.server.ServerCallback
import com.example.wantednews.ui.activity.main.common.adapter.NewsListAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class TopNewsFragment : Fragment() {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private var topNewsListAdapter: NewsListAdapter? = null
    private val serverCallback = object : ServerCallback {
        override fun <Any> onSuccessResponse(call: Call<Any>, response: Response<Any>) {
            binding.progressBar.isVisible = false
            val data = response.body() as TopHeadlinesData.TopHeadlines

            topNewsListAdapter = NewsListAdapter(data.articles, onClickListener)
            binding.newsList.adapter = topNewsListAdapter
        }

        override fun <Any> onFailResponse(call: Call<Any>, responseCode: Int?, response: ResponseBody?, t: Throwable?) {
            binding.progressBar.isVisible = false
            PopupWindow()
        }
    }

    private val onClickListener = object : NewsListAdapter.OnClickListener {
        override fun onItemClickListener(article: TopHeadlinesData.Article) {
            val bundle = Bundle().apply { putParcelable(Constants.BundleTag.TAG_NEWS_INFO, article) }
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_news_detail_fragment, bundle)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)

        initData()
        initLayout()
        initListener()

        requestNewsList()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {

    }

    private fun initLayout() {

    }

    private fun initListener() {

    }

    private fun requestNewsList() {
        binding.progressBar.isVisible = true
        ServerApi.getTopHeadlines(serverCallback, Constants.Countries.COUNTRY_KR, null, null, null, null)
    }
}
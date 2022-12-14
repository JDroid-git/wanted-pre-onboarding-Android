package com.example.wantednews.ui.activity.main.fragment.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.wantednews.R
import com.example.wantednews.constants.Constants
import com.example.wantednews.data.TopHeadlinesData
import com.example.wantednews.databinding.FragmentNewsListBinding
import com.example.wantednews.room.SaveArticleDatabase
import com.example.wantednews.ui.activity.main.common.adapter.NewsListAdapter

class SavedFragment : Fragment() {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!
    private var savedListAdapter: NewsListAdapter? = null

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
        initObserve()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {

    }

    private fun initLayout() {
        savedListAdapter = NewsListAdapter(onClickListener)
        binding.newsList.adapter = savedListAdapter

        binding.swipeLayout.isEnabled = false
    }

    private fun initListener() {

    }

    private fun initObserve() {
        SaveArticleDatabase.getInstance(requireContext().applicationContext)?.articleDao()?.getSavedList()?.observe(viewLifecycleOwner) { savedList ->
            savedListAdapter?.updateNews(ArrayList(savedList.map { it.article }))
        }
    }
}
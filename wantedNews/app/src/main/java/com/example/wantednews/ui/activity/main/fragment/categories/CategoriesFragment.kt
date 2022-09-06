package com.example.wantednews.ui.activity.main.fragment.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.wantednews.R
import com.example.wantednews.constants.Constants
import com.example.wantednews.databinding.FragmentCategoriesBinding
import com.example.wantednews.ui.activity.main.fragment.categories.adapter.CategoriesAdapter

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private var categoryList: ArrayList<Pair<String, Int>> = arrayListOf()

    private val onClickListener = object : CategoriesAdapter.OnClickListener {
        override fun onItemClickListener(position: Int) {
            val bundle = Bundle().apply { putString(Constants.BundleTag.TAG_CATEGORY, categoryList[position].first) }
            requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_category_news_list_fragment, bundle)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        initData()
        initLayout()
        initListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {
        val categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        categoryList = categoryViewModel.categoryList
    }

    private fun initLayout() {

        binding.categoryList.adapter = CategoriesAdapter(categoryList, onClickListener)
    }

    private fun initListener() {

    }
}
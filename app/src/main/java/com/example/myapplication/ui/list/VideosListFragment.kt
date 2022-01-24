package com.example.myapplication.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentVideosListBinding
import com.example.myapplication.ui.NavToolBar
import com.example.myapplication.viewmodel.VideosViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.model.Video
import com.example.myapplication.showSnackBar
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.details.VideoFragment
import com.example.myapplication.viewmodel.AppState

class VideosListFragment : BaseFragment<VideosViewModel, FragmentVideosListBinding>(FragmentVideosListBinding::inflate) {

    private lateinit var adapter: VideosAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VideosAdapter()
        adapter.setOnClick(object : VideosAdapter.OnClick {
            override fun onClick(video: Video) {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.container, VideoFragment.newInstance(video))
                    ?.addToBackStack("")
                    ?.commit()
            }

            override fun onLongClick(video: Video) {
                return
            }

        })

        if (activity is NavToolBar) {
            (activity as NavToolBar?)!!.supplyToolbar(binding.toolbar)
        }

        with (binding) {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.search -> {
                        true
                    }
                    else -> false
                }
            }
            videosList.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            videosList.adapter = adapter

            swipeToRefresh.setOnRefreshListener { viewModel.getAllVideos() }
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, { state ->
            renderData(state)
        })
        viewModel.getAllVideos()

    }

    private fun renderData(state: AppState) {
        binding.swipeToRefresh.isRefreshing = false
        when (state) {
            is AppState.Success -> {
                binding.empty.isVisible = state.videos.isEmpty()
                adapter.setData(state.videos)

            }
            is AppState.Loading -> {
                with (binding) {
                    empty.isVisible = false
                    swipeToRefresh.isRefreshing = true
                }
            }
            is AppState.Error -> {
                binding.empty.isVisible = true
                binding.root.showSnackBar(
                    text =state.error.toString(),
                    actionText = R.string.retry,
                    {viewModel.getAllVideos()}
                )
            }
        }
    }
}
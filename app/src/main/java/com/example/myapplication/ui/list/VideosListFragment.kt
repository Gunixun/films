package com.example.myapplication.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentVideosListBinding
import com.example.myapplication.ui.NavToolBar
import com.example.myapplication.viewmodel.VideosViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.model.Video
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.details.VideoFragment
import com.example.myapplication.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

class VideosListFragment : BaseFragment<VideosViewModel, FragmentVideosListBinding>(FragmentVideosListBinding::inflate) {

    private lateinit var adapter: VideosAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VideosAdapter()
        adapter.setOnClick(object : VideosAdapter.OnClick {
            override fun onClick(video: Video) {
                val manager = activity?.supportFragmentManager
                manager
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

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    true
                }
                else -> false
            }
        }

        binding.videosList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.videosList.adapter = adapter

        binding.swipeToRefresh.setOnRefreshListener { viewModel.getAllVideos() }

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> { state ->
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
                binding.empty.isVisible = false
                binding.swipeToRefresh.isRefreshing = true
            }
            is AppState.Error -> {
                binding.empty.isVisible = true
                Snackbar
                    .make(binding.root, state.error.toString(), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) { viewModel.getAllVideos() }
                    .show()

            }
        }
    }
}
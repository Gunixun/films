package com.example.myapplication.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentVideosListBinding
import com.example.myapplication.ui.NavToolBar
import com.example.myapplication.viewmodel.VideosViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.myapplication.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

class VideosListFragment : Fragment() {

    private lateinit var model: VideosViewModel
    private lateinit var adapter: VideosAdapter

    private var _binding: FragmentVideosListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideosListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VideosAdapter()

        if (activity is NavToolBar) {
            (activity as NavToolBar?)!!.supplyToolbar(binding.toolbar)
        }

        binding.toolbar.setOnMenuItemClickListener{menuItem ->
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

        model = VideosViewModel()

        binding.swipeToRefresh.setOnRefreshListener{ model.getAllVideos() }

        model.getLiveData().observe(viewLifecycleOwner, Observer<AppState>{ state ->
            renderData(state)
        })
        model.getAllVideos()

    }

    private fun renderData(state: AppState) {
        binding.swipeToRefresh.isRefreshing = false
        when (state){
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
                    .setAction(R.string.retry) { model.getAllVideos() }
                    .show()

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
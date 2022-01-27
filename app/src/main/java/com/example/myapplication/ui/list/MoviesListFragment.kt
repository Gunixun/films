package com.example.myapplication.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMoviesListBinding
import com.example.myapplication.ui.NavToolBar
import com.example.myapplication.viewmodel.MoviesViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.showSnackBar
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.ui.details.MovieFragment
import com.example.myapplication.viewmodel.AppState

class MoviesListFragment :
    BaseFragment<MoviesViewModel, FragmentMoviesListBinding>(FragmentMoviesListBinding::inflate) {

    private lateinit var adapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesAdapter()
        adapter.setOnClick(object : MoviesAdapter.OnClick {
            override fun onClick(moviePreview: MoviePreview) {
                val manager = activity?.supportFragmentManager
                manager
                    ?.beginTransaction()
                    ?.replace(R.id.container, MovieFragment.newInstance(moviePreview))
                    ?.addToBackStack("")
                    ?.commit()
            }

            override fun onLongClick(moviePreview: MoviePreview) {
                return
            }

        })

        if (activity is NavToolBar) {
            (activity as NavToolBar?)!!.supplyToolbar(binding.toolbar)
        }

        with(binding) {
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

            binding.swipeToRefresh.setOnRefreshListener { viewModel.getAllMovies() }

            viewModel.getLiveData().observe(viewLifecycleOwner, { state ->
                renderData(state)
            })
            viewModel.getAllMovies()

        }
    }

    private fun renderData(state: AppState) {
        binding.swipeToRefresh.isRefreshing = false
        when (state) {
            is AppState.Success -> {
                binding.empty.isVisible = state.movies.isEmpty()
                adapter.setData(state.movies)

            }
            is AppState.Loading -> {
                with(binding) {
                    empty.isVisible = false
                    swipeToRefresh.isRefreshing = true
                }
            }
            is AppState.Error -> {
                binding.empty.isVisible = true
                binding.root.showSnackBar(
                    text = state.error.toString(),
                    actionText = R.string.retry,
                    { viewModel.getAllMovies() }
                )
            }
        }
    }
}
package com.example.myapplication.ui.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMoviesListBinding
import com.example.myapplication.ui.NavToolBar
import com.example.myapplication.viewmodel.MoviesViewModel
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.showSnackBar
import com.example.myapplication.ui.BaseFragmentWithModel
import com.example.myapplication.ui.details.MovieFragment
import com.example.myapplication.utils.Constants
import com.example.myapplication.utils.TypeMovies
import com.example.myapplication.viewmodel.AppState
import kotlin.properties.Delegates

class MoviesListFragment :
    BaseFragmentWithModel<MoviesViewModel, FragmentMoviesListBinding>(FragmentMoviesListBinding::inflate) {

    private lateinit var adapter: MoviesAdapter
    private var adult by Delegates.notNull<Boolean>()

    companion object {
        const val ARG_PARAM = "Movie Type"
        var movieType: TypeMovies? = null

        fun newInstance(type: TypeMovies): MoviesListFragment {
            return MoviesListFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    bundle.putString(ARG_PARAM, type.toString())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            adult = it.getPreferences(Context.MODE_PRIVATE).getBoolean(Constants.IS_ADULT_KEY, true)
        }

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

        movieType = arguments?.getString(ARG_PARAM)?.let { TypeMovies.valueOf(it) }
        if (movieType == null){
            movieType = TypeMovies.NOW_PLAYING
        }

        setTitle()

        with(binding) {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.search -> {
                        true
                    }
                    else -> false
                }
            }
            moviesList.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            moviesList.adapter = adapter

            binding.swipeToRefresh.setOnRefreshListener { viewModel.getMovies(adult, movieType!!) }

            viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
                renderData(state)
            }
            viewModel.getMovies(adult, movieType!!)

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
                    { viewModel.getMovies(adult, movieType!!) }
                )
            }
        }
    }

    private fun setTitle(){
        when (movieType) {
            TypeMovies.NOW_PLAYING -> binding.toolbar.title = getString(R.string.now_playing)
            TypeMovies.TOP_RATED -> binding.toolbar.title = getString(R.string.top_rated)
            TypeMovies.HISTORY -> binding.toolbar.title = getString(R.string.history)
            TypeMovies.POPULAR -> binding.toolbar.title = getString(R.string.popular)
            TypeMovies.BOOKMARKS -> binding.toolbar.title = getString(R.string.bookmarks)
        }
    }
}
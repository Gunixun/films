package com.example.myapplication.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMovieBinding
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.showSnackBar
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.viewmodel.AppStateMovie
import com.example.myapplication.viewmodel.MovieViewModel


class MovieFragment :
    BaseFragment<MovieViewModel, FragmentMovieBinding>(FragmentMovieBinding::inflate) {

    companion object {
        const val ARG_PARAM = "video"
        var movie: MoviePreview? = null

        fun newInstance(movie: MoviePreview): MovieFragment {
            return MovieFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    bundle.putParcelable(ARG_PARAM, movie)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie = arguments?.getParcelable<MoviePreview>(ARG_PARAM)
        if (movie != null) {
            viewModel.getMovie(movie!!)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            renderData(state)
        }

    }

    private fun renderData(state: AppStateMovie) {
        binding.progress.isVisible = false
        when (state) {
            is AppStateMovie.Success -> {
                with(state.movie) {
                    binding.titleTextview.text = title
                    binding.originTitleTextview.text = original_title
                    binding.averageTextview.text = "Год: $release_year\nРейтинг: $average"
                    binding.shortInfoTextview.text = """
Жанры: ${genres.joinToString(separator = ", ")}
Страна:
"""
                    binding.overviewTextview.text = overview
                }

            }
            is AppStateMovie.Loading -> {
                binding.progress.isVisible = true
            }
            is AppStateMovie.Error -> {
                binding.root.showSnackBar(
                    text = state.error.toString(),
                    actionText = R.string.retry,
                    { viewModel.getMovie(movie!!) }
                )
            }
        }
    }
}
package com.example.myapplication.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMovieBinding
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.showSnackBar
import com.example.myapplication.ui.BaseFragmentWithModel
import com.example.myapplication.utils.DETAILS_POSTER_SIZE
import com.example.myapplication.utils.MAIN_POSTER_LINK
import com.example.myapplication.viewmodel.AppStateMovie
import com.example.myapplication.viewmodel.MovieViewModel


class MovieFragment :
    BaseFragmentWithModel<MovieViewModel, FragmentMovieBinding>(FragmentMovieBinding::inflate) {

    companion object {
        const val ARG_PARAM = "Movie ID"
        var movieId: String? = ""

        fun newInstance(movie: MoviePreview): MovieFragment {
            return MovieFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    bundle.putString(ARG_PARAM, movie.id)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieId = arguments?.getString(ARG_PARAM)
        if (movieId != null) {
            viewModel.getMovie(movieId!!)
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
                    Glide
                        .with(requireContext())
                        .load("$MAIN_POSTER_LINK$DETAILS_POSTER_SIZE${state.movie.icon_path}")
                        .into(binding.imageView);
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
                    { viewModel.getMovie(movieId!!) }
                )
            }
        }
    }
}
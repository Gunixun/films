package com.example.myapplication.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMovieBinding
import com.example.myapplication.model.ActorPreview
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.showSnackBar
import com.example.myapplication.ui.BaseFragmentWithModel
import com.example.myapplication.ui.actor_details.ActorFragment
import com.example.myapplication.utils.DETAILS_POSTER_SIZE
import com.example.myapplication.utils.MAIN_POSTER_LINK
import com.example.myapplication.viewmodel.AppStateMovie
import com.example.myapplication.viewmodel.MovieViewModel


class MovieFragment :
    BaseFragmentWithModel<MovieViewModel, FragmentMovieBinding>(FragmentMovieBinding::inflate) {

    private lateinit var adapter: ActorsAdapter

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

        adapter = ActorsAdapter()
        adapter.setOnClick(object : ActorsAdapter.OnClick {
            override fun onClick(actorPreview: ActorPreview) {
                val manager = activity?.supportFragmentManager
                manager
                    ?.beginTransaction()
                    ?.replace(R.id.container, ActorFragment.newInstance(actorPreview))
                    ?.addToBackStack("Transaction")
                    ?.commit()
            }

            override fun onLongClick(actorPreview: ActorPreview) {
                return
            }

        })

        movieId = arguments?.getString(ARG_PARAM)
        if (movieId != null) {
            viewModel.getMovie(movieId!!)
        }
        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            renderData(state)
        }

        binding.actorsList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.actorsList.adapter = adapter

    }

    private fun renderData(state: AppStateMovie) {
        binding.progress.isVisible = false
        when (state) {
            is AppStateMovie.Success -> {
                setMovies(state.movie)

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

    @SuppressLint("SetTextI18n")
    private fun setMovies(movie: Movie){
        viewModel.saveHistory(movie)
        with(binding) {
            Glide
                .with(requireContext())
                .load("${MAIN_POSTER_LINK}${DETAILS_POSTER_SIZE}${movie.icon_path}")
                .into(imageView);
            titleTextview.text = movie.title
            originTitleTextview.text = movie.original_title
            averageTextview.text = "Год: ${movie.release_year}\nРейтинг: ${movie.average}"
            shortInfoTextview.text = """
Жанры: ${movie.genres.joinToString(separator = ", ")}
Страна:
"""
            overviewTextview.text = movie.overview
            adapter.setData(movie.actors)
        }
    }
}
package com.example.myapplication.ui.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMovieBinding
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.showSnackBar


const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val MOVIE_ID_EXTRA = "MOVIE ID EXTRA"
const val DETAILS_MOVIE_EXTRA = "DETAILS MOVIE EXTRA"


class MovieDetailServiceFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_PARAM = "video"
        var movie: MoviePreview? = null

        fun newInstance(movie: MoviePreview): MovieDetailServiceFragment {
            return MovieDetailServiceFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    bundle.putParcelable(MovieFragment.ARG_PARAM, movie)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movie = arguments?.getParcelable<MoviePreview>(ARG_PARAM)
        if (movie != null) {
            getMovie()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        context?.unregisterReceiver(loadResultsReceiver)
    }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> onError()
                DETAILS_REQUEST_ERROR_EXTRA -> onError()
                DETAILS_RESPONSE_SUCCESS_EXTRA -> displayMovie(
                    intent.getParcelableExtra<Movie>(DETAILS_MOVIE_EXTRA)
                )
                else -> onError()
            }
        }
    }

    private fun displayMovie(movie: Movie?) {
        if (movie == null) {
            return
        }
        binding.progress.isVisible = false
        with(movie) {
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

    private fun onError() {
        binding.root.showSnackBar(
            text = "Ошибка при загрузке данных",
            actionText = R.string.retry,
            { getMovie() }
        )
    }

    private fun getMovie() {
        binding.progress.isVisible = true
        context?.let {
            it.startService(Intent(it, DetailService::class.java).apply {
                putExtra(
                    MOVIE_ID_EXTRA,
                    movie!!.id
                )
            })
        }

    }
}
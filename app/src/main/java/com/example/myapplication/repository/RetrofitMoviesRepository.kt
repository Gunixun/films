package com.example.myapplication.repository

import com.example.myapplication.BuildConfig
import com.example.myapplication.model.*
import com.example.myapplication.repository.api.GenresApi
import com.example.myapplication.repository.api.MovieApi
import com.example.myapplication.repository.api.MoviesApi
import com.example.myapplication.repository.dto.GenresDTO
import com.example.myapplication.repository.dto.MovieDTO
import com.example.myapplication.repository.dto.MoviesDTO
import com.example.myapplication.utils.*
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitMoviesRepository : IRepository {

    private var jenresMovies: MutableMap<Int, String> = mutableMapOf()

    private fun getGenresMovies() {

        val retrofit = Retrofit.Builder()
            .baseUrl(MAIN_LINK)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            )
            .build().create(GenresApi::class.java)
        retrofit.getGenres(BuildConfig.MOVIE_API_KEY)
            .enqueue(object : Callback<GenresDTO> {
                override fun onResponse(call: Call<GenresDTO>, response: Response<GenresDTO>) {
                    val genresDTO: GenresDTO? = response.body()
                    if (genresDTO != null) {
                        for (genreDTO in genresDTO.genres) {
                            jenresMovies[genreDTO.id] = genreDTO.name
                        }
                    }
                }

                override fun onFailure(call: Call<GenresDTO>, e: Throwable) {
                }
            })
    }

    override fun getMovies(adult: Boolean, movieType: TypeMovies, callback: CallbackData<List<MoviePreview>>) {
        if (jenresMovies.isEmpty()) {
            getGenresMovies()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(MAIN_LINK)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            )
            .build().create(MoviesApi::class.java)
        retrofit.getMovies(getCatalog(movieType), BuildConfig.MOVIE_API_KEY)
            .enqueue(object : Callback<MoviesDTO> {
                override fun onResponse(call: Call<MoviesDTO>, response: Response<MoviesDTO>) {
                    val moviesDTO: MoviesDTO? = response.body()
                    if (moviesDTO != null) {
                        callback.onSuccess(convertMoviesDTO(adult, moviesDTO, jenresMovies))
                    } else {
                        callback.onError(Throwable())
                    }
                }

                override fun onFailure(call: Call<MoviesDTO>, e: Throwable) {
                    callback.onError(e)
                }
            })

    }

    override fun getMovie(movieId: String, callback: CallbackData<Movie>) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MAIN_LINK)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            )
            .build().create(MovieApi::class.java)
        retrofit.getMovie(movieId, BuildConfig.MOVIE_API_KEY)
            .enqueue(object : Callback<MovieDTO> {
                override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
                    val movieDTO: MovieDTO? = response.body()
                    if (movieDTO != null) {
                        callback.onSuccess(convertMovieDTO(movieDTO))
                    } else {
                        callback.onError(Throwable())
                    }

                }

                override fun onFailure(call: Call<MovieDTO>, e: Throwable) {
                    callback.onError(e)
                }
            })
    }

}
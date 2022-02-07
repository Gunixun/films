package com.example.myapplication.repository

import com.example.myapplication.BuildConfig
import com.example.myapplication.model.*
import com.example.myapplication.model.dto.GenresDTO
import com.example.myapplication.model.dto.MovieDTO
import com.example.myapplication.model.dto.MoviesDTO
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
                        callback.onSuccess(convertDTO(adult, moviesDTO, jenresMovies))
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
        if (jenresMovies.isEmpty()) {
            getGenresMovies()
        }

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
                        val genres: MutableList<String> = mutableListOf()
                        for (genre in movieDTO.genres) {
                            jenresMovies.get(genre.id)?.let { genres.add(it) }
                        }
                        callback.onSuccess(
                            Movie(
                                title = movieDTO.title,
                                original_title = movieDTO.original_title,
                                average = movieDTO.vote_average.toString(),
                                genres = genres,
                                id = movieDTO.id,
                                icon_path = movieDTO.poster_path,
                                release_year = movieDTO.release_date.slice(0..3),
                                overview = movieDTO.overview,
                            )
                        )
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
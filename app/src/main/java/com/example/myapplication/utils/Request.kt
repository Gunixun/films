package com.example.myapplication.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.BuildConfig
import java.io.BufferedReader
import java.util.stream.Collectors

const val MAIN_LINK = "https://api.themoviedb.org/"
const val API_KEY = "api_key=${BuildConfig.MOVIE_API_KEY}"
const val LANGUAGE = "language=ru-RU"

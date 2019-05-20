package com.example.moviedbclean.domain.repository

import com.example.moviedbclean.data.remote.response.MovieListResultResponse
import io.reactivex.Single

interface MovieRepository {
    fun discoverMovie(): Single<List<MovieListResultResponse>>
}
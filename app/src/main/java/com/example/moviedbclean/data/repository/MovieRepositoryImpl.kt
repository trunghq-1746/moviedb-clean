package com.example.moviedbclean.data.repository

import com.example.moviedbclean.data.remote.MovieAPIService
import com.example.moviedbclean.data.remote.response.MovieListResultResponse
import com.example.moviedbclean.domain.repository.MovieRepository
import io.reactivex.Single

class MovieRepositoryImpl(
    private val apiService: MovieAPIService
): MovieRepository {
    override fun discoverMovie(): Single<List<MovieListResultResponse>> {
        return apiService.discoverMovie().map {
            it.results
        }
    }
}
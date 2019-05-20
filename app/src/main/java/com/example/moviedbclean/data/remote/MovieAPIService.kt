package com.example.moviedbclean.data.remote

import com.example.moviedbclean.data.remote.response.DiscoverMovieResponse
import io.reactivex.Single
import retrofit2.http.GET

interface MovieAPIService {

    @GET(EndPoint.DISCOVER_MOVIE)
    fun discoverMovie(): Single<DiscoverMovieResponse>

    object Params {
    }

    object EndPoint {
        const val DISCOVER_MOVIE = "discover/movie"
    }
}
package com.example.moviedbclean.data.remote.response

import com.example.moviedbclean.domain.response.MovieDomainResponse
import com.google.gson.annotations.SerializedName

data class DiscoverMovieResponse (
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val results: List<MovieListResultResponse>,
        @SerializedName("total_results")
        val totalResults: Int,
        @SerializedName("total_pages")
        val totalPages: Int
) {
        fun mapToDomain(): List<MovieDomainResponse> {
                return results.map {
                        it.mapToDomain()
                }
        }
}
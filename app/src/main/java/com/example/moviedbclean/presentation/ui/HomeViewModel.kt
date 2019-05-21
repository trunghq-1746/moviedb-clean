package com.example.moviedbclean.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.example.moviedbclean.domain.useCase.DiscoverMovieUseCase
import com.example.moviedbclean.presentation.model.MovieModel

class HomeViewModel(
    private val discoverMovieUseCase: DiscoverMovieUseCase
) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>>
        get() = _movies
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        discoverMovie()
    }

    fun discoverMovie() {
        discoverMovieUseCase.execute {
            onLoading { isLoading, progress ->
                _isLoading.value = isLoading
            }
            onComplete {
                _movies.value = it
            }
            onError {

            }
        }
    }
}

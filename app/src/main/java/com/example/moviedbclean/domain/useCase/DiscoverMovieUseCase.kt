package com.example.moviedbclean.domain.useCase

import com.example.moviedbclean.data.repository.MovieRepositoryImpl
import com.example.moviedbclean.domain.response.MovieDomainResponse
import com.example.moviedbclean.presentation.model.MovieModel
import io.reactivex.Single

class DiscoverMovieUseCase(
        private val repository: MovieRepositoryImpl
) : BaseUseCase<List<MovieModel>, Single<List<MovieDomainResponse>>>() {

    override fun buildUseCaseObservable(): Single<List<MovieDomainResponse>> {
        return repository.discoverMovie().map {
            it.map { dataModel ->
                dataModel.mapToDomain()
            }
        }
    }

    override fun execute(block: OnCompletionCallback<List<MovieModel>>.() -> Unit) {
        executeSingle(block)
    }

}
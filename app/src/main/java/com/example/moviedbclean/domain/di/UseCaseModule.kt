package com.example.moviedbclean.domain.di

import com.example.moviedbclean.domain.useCase.DiscoverMovieUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { DiscoverMovieUseCase(get()) }
}
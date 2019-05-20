package com.example.moviedbclean.data.di

import com.example.moviedbclean.data.repository.MovieRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single { MovieRepositoryImpl(get()) }
}
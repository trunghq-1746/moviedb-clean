package com.example.moviedbclean.presentation.di

import com.example.moviedbclean.presentation.ui.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}
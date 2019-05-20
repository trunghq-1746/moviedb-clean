package com.example.moviedbclean

import android.app.Application
import com.example.moviedbclean.data.di.networkModule
import com.example.moviedbclean.data.di.repositoryModule
import com.example.moviedbclean.domain.di.useCaseModule
import com.example.moviedbclean.presentation.di.appModule
import com.example.moviedbclean.presentation.di.viewModelModule
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule, viewModelModule, useCaseModule, repositoryModule, networkModule)
        }
    }
}
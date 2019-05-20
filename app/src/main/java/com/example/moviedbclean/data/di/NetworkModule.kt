package com.example.moviedbclean.data.di

import com.example.moviedbclean.BuildConfig
import com.example.moviedbclean.data.remote.MovieAPIService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { createHeaderInterceptor() }
    single { createOkHttpClient(get()) }
    single { createAppRetrofit(get()) }
    single { createMovieAPIService(get()) }
}

fun createHeaderInterceptor(): Interceptor {
    return Interceptor {
        val request = it.request()
        val newUrl = request.url().newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()
        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        it.proceed(newRequest)
    }
}

fun createOkHttpClient(header: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(header)
            .build()
}

fun createAppRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
}

fun createMovieAPIService(retrofit: Retrofit): MovieAPIService {
    return retrofit.create(MovieAPIService::class.java)
}
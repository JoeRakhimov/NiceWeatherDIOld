package com.joerakhimov.niceweatherdi.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.joerakhimov.niceweatherdi.data.Api
import com.joerakhimov.niceweatherdi.data.BASE_URL
import com.joerakhimov.niceweatherdi.data.RemoteRepository
import com.joerakhimov.niceweatherdi.data.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    //    @Named("HttpInterceptor")
    @Provides
    @HttpInterceptor
    fun provideHttpInterceptor(@ApplicationContext context: Context): Interceptor =
        ChuckerInterceptor.Builder(context).build()

    //    @Named("LoggingInterceptor")
    @Provides
    @LoggingInterceptor
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    @Provides
    fun provideOkHttpClient(
//        @Named("HttpInterceptor") chuckerInterceptor: Interceptor,
        @HttpInterceptor httpInterceptor: Interceptor,
//        @Named("LoggingInterceptor") loggingInterceptor: Interceptor
        @LoggingInterceptor loggingInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient): Api =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

//    @Provides
//    fun provideRepository(api: Api): Repository = RemoteRepository(api)

    @InstallIn(SingletonComponent::class)
    @Module
    interface Bindings {
        @Binds
        fun bindRepository(repository: RemoteRepository): Repository
    }

}
package com.example.burgers.core.di

import android.util.Log
import com.google.gson.GsonBuilder
import com.example.burgers.BuildConfig
import com.example.burgers.data.datasource.api.BurgersApi
import com.example.burgers.data.datasource.api.fake.BurgersApiFakeInterceptor
import com.example.burgers.domain.model.RemoteBurger
import com.example.burgers.domain.model.RemoteBurgerDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHeaderLoggingInterceptor(): HttpLoggingInterceptor {
        val headerHttpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Log.i("HeadersLoggingInterceptor", message) }
        headerHttpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.NONE
        return headerHttpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideBodyLoggingInterceptor(): HttpLoggingInterceptor {
        val headerHttpLoggingInterceptor =
            HttpLoggingInterceptor { message -> Log.i("BodyLoggingInterceptor", message) }
        headerHttpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return headerHttpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
            val url = request.url
                .newBuilder()
                .build()

            builder.url(url)
            builder.addHeader(
                "X-RapidAPI-Key",
                "12ac1373d5msh014658f11ba85e4p196eabjsn8b91b1277e3b"
            )
            builder.addHeader("X-RapidAPI-Host", "burgers-hub.p.rapidapi.com")
            chain.proceed(builder.build())
        }
    }

    @Provides
    @Singleton
    fun provideApiFakeInterceptor(): Interceptor {
        return BurgersApiFakeInterceptor()
    }


    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(provideHeaderInterceptor())
            .addInterceptor(provideHeaderLoggingInterceptor())
            .addInterceptor(provideBodyLoggingInterceptor())
            //As per the limitation of the rapid-api 10 free calls only so this is a fake response with the same data
            .addInterceptor(provideApiFakeInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        val remoteBurgerDeserializer =
            GsonBuilder().registerTypeAdapter(RemoteBurger::class.java, RemoteBurgerDeserializer())
                .create()

        return Retrofit.Builder()
            .baseUrl("https://burgers-hub.p.rapidapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(remoteBurgerDeserializer))
            .build()
    }

    @Provides
    @Singleton
    fun provideBurgersApi(retrofit: Retrofit): BurgersApi {
        return retrofit.create(BurgersApi::class.java)
    }

}
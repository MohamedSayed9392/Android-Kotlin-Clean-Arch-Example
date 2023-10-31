package com.example.burgers.helpers

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.gson.GsonBuilder
import com.example.burgers.data.datasource.api.BurgersApi
import com.example.burgers.data.datasource.api.fake.BurgersApiFakeInterceptor
import com.example.burgers.data.datasource.db.AssetsBurgersDatabase
import com.example.burgers.domain.model.AssetsBurger
import com.example.burgers.domain.model.RemoteBurger
import com.example.burgers.domain.model.RemoteBurgerDeserializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TestHelpers {
    fun getHttpsTestDBItem(): AssetsBurger {
        return AssetsBurger(
            link = "https://www.facebook.com"
        )
    }

    fun getHttpTestDBItem(): AssetsBurger {
        return AssetsBurger(
            link = "http://www.google.com"
        )
    }

    fun getWithoutHttpTestDBItem(): AssetsBurger {
        return AssetsBurger(
            link = "www.google.com"
        )
    }

    fun getTestDB(): AssetsBurgersDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(
            context,
            AssetsBurgersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    fun getTestRetrofitApiService(isConnected: Boolean = true): BurgersApi {
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(if (isConnected) BurgersApiFakeInterceptor() else NoInternetInterceptor())
            .build()

        val remoteBurgerDeserializer =
            GsonBuilder().registerTypeAdapter(RemoteBurger::class.java, RemoteBurgerDeserializer())
                .create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(remoteBurgerDeserializer))
            .baseUrl("https://burgers-hub.p.rapidapi.com/")
            .client(okHttpClient)
            .build().create(BurgersApi::class.java)
    }
}
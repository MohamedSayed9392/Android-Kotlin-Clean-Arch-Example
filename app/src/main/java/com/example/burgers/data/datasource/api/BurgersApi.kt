package com.example.burgers.data.datasource.api

import com.example.burgers.domain.model.RemoteBurger
import retrofit2.Response
import retrofit2.http.GET

interface BurgersApi {
    @GET("burgers")
    suspend fun getBurgers(): Response<List<RemoteBurger>>
}
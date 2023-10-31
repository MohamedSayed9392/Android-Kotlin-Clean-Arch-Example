package com.example.burgers.domain.repository

import com.example.burgers.domain.model.AssetsBurger
import com.example.burgers.domain.model.RemoteBurger
import kotlinx.coroutines.flow.Flow

interface AssetsBurgerRepository {
    suspend fun getAllBurgersDB(): Flow<List<AssetsBurger>>
    suspend fun getBurgersRemote(): Flow<List<RemoteBurger>>
}
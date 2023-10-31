package com.example.burgers.data.repository

import com.example.burgers.data.datasource.api.BurgersApi
import com.example.burgers.data.datasource.dao.AssetsBurgersDao
import com.example.burgers.domain.model.AssetsBurger
import com.example.burgers.domain.model.RemoteBurger
import com.example.burgers.domain.repository.AssetsBurgerRepository
import kotlinx.coroutines.flow.*
import java.net.UnknownHostException

class AssetsBurgerRepositoryImpl(
    private val dao: AssetsBurgersDao,
    private val api: BurgersApi
) : AssetsBurgerRepository {

    override suspend fun getAllBurgersDB(): Flow<List<AssetsBurger>> {
        return  dao.getAllBurgers(startWithQuery = "%")
    }

    override suspend fun getBurgersRemote(): Flow<List<RemoteBurger>> {
        return flow {
            try {
                api.getBurgers().body()?.let { list ->
                    emit(list)
                } ?: run {
                    emit(arrayListOf())
                }
            }catch (e: UnknownHostException){ //In case no Internet
                emit(arrayListOf())
            }
        }
    }
}
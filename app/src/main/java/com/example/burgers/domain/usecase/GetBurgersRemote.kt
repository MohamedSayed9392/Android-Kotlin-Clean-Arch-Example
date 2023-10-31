package com.example.burgers.domain.usecase

import com.example.burgers.domain.model.AssetsBurger
import com.example.burgers.domain.model.RemoteBurger
import com.example.burgers.domain.repository.AssetsBurgerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetBurgersRemote(
    private val repository: AssetsBurgerRepository
) {

    suspend operator fun invoke(): Flow<List<RemoteBurger>> {
        return repository.getBurgersRemote()
    }
}
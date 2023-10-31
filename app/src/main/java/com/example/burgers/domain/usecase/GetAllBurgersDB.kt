package com.example.burgers.domain.usecase

import com.example.burgers.domain.model.AssetsBurger
import com.example.burgers.domain.repository.AssetsBurgerRepository
import kotlinx.coroutines.flow.Flow

class GetAllBurgersDB(
    private val repository: AssetsBurgerRepository
) {

    suspend operator fun invoke(): Flow<List<AssetsBurger>> {
        return repository.getAllBurgersDB()
    }
}
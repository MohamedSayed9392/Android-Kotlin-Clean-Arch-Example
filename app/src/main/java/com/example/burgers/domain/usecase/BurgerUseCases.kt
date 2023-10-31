package com.example.burgers.domain.usecase

data class BurgerUseCases(
    val getAllBurgersDB: GetAllBurgersDB,
    val getBurgersRemote: GetBurgersRemote
)

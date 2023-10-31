package com.example.burgers.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgers.domain.usecase.BurgerUseCases
import com.example.burgers.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AllBurgersViewModel @Inject constructor(
    private val burgerUseCases: BurgerUseCases
) : ViewModel() {

    private var getAllBurgersJob: Job? = null

    private val _state: MutableStateFlow<List<Any>?> = MutableStateFlow(null)
    val state: MutableStateFlow<List<Any>?> = _state

    init {
        viewModelScope.launch {
            getAllBurgers()
        }
    }

    private suspend fun getAllBurgers() {
        getAllBurgersJob?.cancel()
        getAllBurgersJob = burgerUseCases.getAllBurgersDB()
            .combine(burgerUseCases.getBurgersRemote()) { listDB, listRemote ->
                _state.value = Utils.getShuffledListOfLists(listDB,listRemote)
            }.launchIn(viewModelScope)
    }
}
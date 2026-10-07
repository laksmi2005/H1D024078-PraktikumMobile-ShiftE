package com.pemmob.mealoo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.mealoo.data.model.Meal
import com.pemmob.mealoo.data.repository.MealRepository
import com.pemmob.mealoo.data.repository.MealRepositoryImpl
import com.pemmob.mealoo.ui.common.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(
    private val repository: MealRepository = MealRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Meal>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Meal>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        searchMeals("")

        viewModelScope.launch {
            _searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { query ->
                    searchMeals(query)
                }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun retry() {
        searchMeals(_searchQuery.value)
    }

    private fun searchMeals(query: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.searchMeals(query).fold(
                onSuccess = { meals ->
                    if (meals.isEmpty()) {
                        _uiState.value = UiState.Empty("Resep tidak ditemukan")
                    } else {
                        _uiState.value = UiState.Success(meals)
                    }
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(
                        message = error.localizedMessage ?: "Terjadi kesalahan saat memuat data",
                        throwable = error
                    )
                }
            )
        }
    }
}

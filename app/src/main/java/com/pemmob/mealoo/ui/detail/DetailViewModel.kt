package com.pemmob.mealoo.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pemmob.mealoo.data.model.Meal
import com.pemmob.mealoo.data.repository.MealRepository
import com.pemmob.mealoo.data.repository.MealRepositoryImpl
import com.pemmob.mealoo.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val mealId: String,
    private val repository: MealRepository = MealRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Meal>>(UiState.Loading)
    val uiState: StateFlow<UiState<Meal>> = _uiState.asStateFlow()

    init {
        loadMealDetail()
    }

    fun retry() {
        loadMealDetail()
    }

    private fun loadMealDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getMealDetail(mealId).fold(
                onSuccess = { meal ->
                    _uiState.value = UiState.Success(meal)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(
                        message = error.localizedMessage ?: "Gagal memuat detail resep",
                        throwable = error
                    )
                }
            )
        }
    }

    class Factory(
        private val mealId: String,
        private val repository: MealRepository = MealRepositoryImpl()
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(mealId, repository) as T
        }
    }
}

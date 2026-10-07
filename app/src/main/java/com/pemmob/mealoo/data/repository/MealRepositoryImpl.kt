package com.pemmob.mealoo.data.repository

import com.pemmob.mealoo.data.model.Meal
import com.pemmob.mealoo.data.model.toDomain
import com.pemmob.mealoo.data.remote.MealApiService
import com.pemmob.mealoo.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealRepositoryImpl(
    private val apiService: MealApiService = RetrofitClient.apiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MealRepository {

    override suspend fun searchMeals(query: String): Result<List<Meal>> = withContext(ioDispatcher) {
        try {
            val response = apiService.searchMeals(query)
            val mealDtos = response.meals ?: emptyList()
            val meals = mealDtos.map { it.toDomain() }
            Result.success(meals)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMealDetail(mealId: String): Result<Meal> = withContext(ioDispatcher) {
        try {
            val response = apiService.lookupMeal(mealId)
            val mealDto = response.meals?.firstOrNull()
            if (mealDto != null) {
                Result.success(mealDto.toDomain())
            } else {
                Result.failure(NoSuchElementException("Meal with id $mealId not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

package com.pemmob.mealoo.data.repository

import com.pemmob.mealoo.data.model.Meal

interface MealRepository {
    suspend fun searchMeals(query: String): Result<List<Meal>>
    suspend fun getMealDetail(mealId: String): Result<Meal>
}

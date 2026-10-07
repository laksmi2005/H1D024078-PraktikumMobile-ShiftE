package com.pemmob.mealoo.data.remote

import com.pemmob.mealoo.data.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): MealResponse

    @GET("lookup.php")
    suspend fun lookupMeal(
        @Query("i") mealId: String
    ): MealResponse
}

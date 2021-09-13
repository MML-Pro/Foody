package com.mml.foody.data

import android.app.DownloadManager
import com.mml.foody.data.network.FoodRecipesAPI
import com.mml.foody.models.FoodJoke
import com.mml.foody.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesAPI: FoodRecipesAPI
) {

    suspend fun getRecipes(queries: Map<String, String>) : Response<FoodRecipe>{
        return foodRecipesAPI.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String,String>) : Response<FoodRecipe>{
        return foodRecipesAPI.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey:String) : Response<FoodJoke>{
        return foodRecipesAPI.getFoodJoke(apiKey)
    }
}
package com.mml.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mml.foody.models.FoodRecipe
import com.mml.foody.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
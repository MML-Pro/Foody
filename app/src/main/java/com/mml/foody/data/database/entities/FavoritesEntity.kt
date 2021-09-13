package com.mml.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mml.foody.models.Result
import com.mml.foody.util.Constants.Companion.FAVORITES_RECIPES_TABLE

@Entity(tableName = FAVORITES_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)
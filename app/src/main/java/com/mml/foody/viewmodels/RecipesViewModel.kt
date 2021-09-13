package com.mml.foody.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.mml.foody.data.DataStoreRepo
import com.mml.foody.util.Constants
import com.mml.foody.util.Constants.Companion.API_KEY
import com.mml.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.mml.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.mml.foody.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.mml.foody.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.mml.foody.util.Constants.Companion.QUERY_API_KEY
import com.mml.foody.util.Constants.Companion.QUERY_DIET
import com.mml.foody.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.mml.foody.util.Constants.Companion.QUERY_NUMBER
import com.mml.foody.util.Constants.Companion.QUERY_SEARCH
import com.mml.foody.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor
    (application: Application, private val dataStoreRepo: DataStoreRepo) :
    AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepo.readMealAndDietType

    var networkStatus = false
    var backOnline = false

    val readBackOnline = dataStoreRepo.readBackOnline.asLiveData()

    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveBackOnline(backOnline)
        }

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun applyQueries(): HashMap<String, String> {

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        val queries = HashMap<String, String>()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries = HashMap<String, String>()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries

    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus && backOnline) {
            Toast.makeText(getApplication(), "Internet is back", Toast.LENGTH_SHORT).show()
            saveBackOnline(false)
        }
    }
}
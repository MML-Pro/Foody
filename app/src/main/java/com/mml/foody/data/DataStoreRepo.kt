package com.mml.foody.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mml.foody.util.Constants.Companion.PREFERENCE_BACK_ONLINE
import com.mml.foody.util.Constants.Companion.PREFERENCE_DIET_TYPE
import com.mml.foody.util.Constants.Companion.PREFERENCE_DIET_TYPE_ID
import com.mml.foody.util.Constants.Companion.PREFERENCE_MEAL_TYPE
import com.mml.foody.util.Constants.Companion.PREFERENCE_MEAL_TYPE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("user_preferences")

@ActivityRetainedScoped
class DataStoreRepo @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCE_MEAL_TYPE)
        val selectedMealTypeID = intPreferencesKey(PREFERENCE_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCE_DIET_TYPE)
        val selectedDietTypeID = intPreferencesKey(PREFERENCE_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCE_BACK_ONLINE)
    }

    private val dataStore = context.dataStore

    suspend fun saveMealAndDietType(
        selectedMealType: String,
        selectedMealTypeID: Int,
        selectedDietType: String,
        selectedDietTypeID: Int,
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedMealType] = selectedMealType
            preferences[PreferencesKeys.selectedMealTypeID] = selectedMealTypeID
            preferences[PreferencesKeys.selectedDietType] = selectedDietType
            preferences[PreferencesKeys.selectedDietTypeID] = selectedDietTypeID
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.backOnline] = backOnline
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { preferences ->
            val selectedMealType =
                preferences[PreferencesKeys.selectedMealType] ?: PREFERENCE_MEAL_TYPE
            val selectedMealTypeID =
                preferences[PreferencesKeys.selectedMealTypeID] ?: 0
            val selectedDietType =
                preferences[PreferencesKeys.selectedDietType] ?: PREFERENCE_DIET_TYPE
            val selectedDietTypeID =
                preferences[PreferencesKeys.selectedDietTypeID] ?: 0

            MealAndDietType(
                selectedMealType,
                selectedMealTypeID,
                selectedDietType,
                selectedDietTypeID,
            )
        }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { preferences ->
            val backOnline = preferences[PreferencesKeys.backOnline] ?: false
            backOnline
        }


}


data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeID: Int,
    val selectedDietType: String,
    val selectedDietTypeID: Int,
)
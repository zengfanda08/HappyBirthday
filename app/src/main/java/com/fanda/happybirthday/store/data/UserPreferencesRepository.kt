package com.fanda.happybirthday.store.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val TAG = "UserPreferencesRepository"
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
    }

    suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout
        }
    }

    // 读取属性值
    val isLinearLayout: Flow<Boolean> = dataStore.data
        // 存在错误时，通过发出 emptyPreferences()，映射函数仍然可以映射到默认值
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        // 使用映射函数将 Flow<Preferences> 转换为 Flow<Boolean>
        // 尚未调用 saveLayoutPreference，该值可能不存在，必须提供一个默认值
        .map { preferences ->
            preferences[IS_LINEAR_LAYOUT] ?: true
        }
}
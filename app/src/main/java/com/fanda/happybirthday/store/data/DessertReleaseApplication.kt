package com.fanda.happybirthday.store.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"

// 创建一个DataStore 对象，扩展给 Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = LAYOUT_PREFERENCE_NAME)

class DessertReleaseApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}
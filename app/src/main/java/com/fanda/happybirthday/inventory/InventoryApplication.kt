package com.fanda.happybirthday.inventory

import android.app.Application
import com.fanda.happybirthday.inventory.data.AppContainer
import com.fanda.happybirthday.inventory.data.AppDataContainer

class InventoryApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

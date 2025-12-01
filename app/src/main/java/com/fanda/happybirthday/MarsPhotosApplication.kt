package com.fanda.happybirthday

import android.app.Application
import com.fanda.happybirthday.mars.data.AppContainer
import com.fanda.happybirthday.mars.data.DefaultAppContainer

class MarsPhotosApplication : Application(){

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer()
    }
}
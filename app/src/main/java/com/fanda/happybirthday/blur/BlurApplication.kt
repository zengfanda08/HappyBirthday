package com.fanda.happybirthday.blur

import android.app.Application
import com.fanda.happybirthday.blur.data.BlurAppContainer
import com.fanda.happybirthday.blur.data.BlurContainer

class BlurApplication  : Application()  {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: BlurContainer
    override fun onCreate() {
        super.onCreate()
        container = BlurAppContainer(this)
    }
}
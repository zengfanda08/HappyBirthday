package com.fanda.happybirthday.blur.data

import android.content.Context

interface BlurContainer {
    val blurRepository: BlurRepository
}

class BlurAppContainer(context: Context) : BlurContainer {
    override val blurRepository = WorkManagerBlurRepository(context)
}
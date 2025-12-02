package com.fanda.happybirthday.blur.data

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface BlurRepository {
    val outputWorkInfo: Flow<WorkInfo?>
    fun applyBlur(blurLevel: Int)
    fun cancelWork()
}
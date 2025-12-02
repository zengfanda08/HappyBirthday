package com.fanda.happybirthday.blur.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.fanda.happybirthday.R
import com.fanda.happybirthday.blur.DELAY_TIME_MILLIS
import com.fanda.happybirthday.blur.KEY_BLUR_LEVEL
import com.fanda.happybirthday.blur.KEY_IMAGE_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "BlurWorker"

class BlurWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    // 挂起函数，通过协程异步执行
    override suspend fun doWork(): Result {

        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)


        // 显示通知
        makeStatusNotification(applicationContext.resources.getString(R.string.blurring_image), applicationContext)

        // CoroutineWorker, 默认以 Dispatchers.Default 的形式运行

        return withContext(Dispatchers.IO) {
            return@withContext try {

                // 模拟耗时工作
                delay(DELAY_TIME_MILLIS)

                // 如果第一个参数的计算结果为 false，该语句会抛出 IllegalArgumentException，描述信息由最后返回的 errorMessage 来定
                require(!resourceUri.isNullOrBlank()) {
                    val errorMessage = applicationContext.resources.getString(R.string.invalid_input_uri)
                    Log.e(TAG, errorMessage)
                    errorMessage
                }

                val resolver = applicationContext.contentResolver

                val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
                val output = blurBitmap(picture, blurLevel)
                val outputUri = writeBitmapToFile(applicationContext, output)
//                makeStatusNotification("Output is $outputUri", applicationContext)

                // 输出数据
                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
                Result.success(outputData)
            } catch (throwable: Throwable) {
                Log.e(
                    TAG, applicationContext.resources.getString(R.string.error_applying_blur), throwable
                )
                Result.failure()
            }
        }


    }

}
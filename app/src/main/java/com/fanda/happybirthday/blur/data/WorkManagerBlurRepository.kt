package com.fanda.happybirthday.blur.data

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.fanda.happybirthday.blur.KEY_BLUR_LEVEL
import com.fanda.happybirthday.blur.KEY_IMAGE_URI
import com.fanda.happybirthday.blur.ui.getImageUri
import com.fanda.happybirthday.blur.workers.BlurWorker
import com.fanda.happybirthday.blur.workers.CleanupWorker
import com.fanda.happybirthday.blur.workers.SaveImageToFileWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WorkManagerBlurRepository(context: Context) : BlurRepository {

    private val workManager = WorkManager.getInstance(context)
    private var imageUri = context.getImageUri()

    override val outputWorkInfo: Flow<WorkInfo?> = MutableStateFlow(null)

    /**
     * Create the WorkRequests to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    override fun applyBlur(blurLevel: Int) {
        // 先创建清理工作
        var continuation = workManager.beginWith(OneTimeWorkRequest.from(CleanupWorker::class.java))


        // 创建请求
        val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
        blurBuilder.setInputData(createInputDataForWorkRequest(blurLevel, imageUri))
        // 开始执行
//        workManager.enqueue(blurBuilder.build())

        // 再添加模糊工作
        continuation = continuation.then(blurBuilder.build())

        // 最后添加保存图片工作
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
        continuation = continuation.then(save.build())

        // 开始执行
        continuation.enqueue()
    }

    /**
     * Cancel any ongoing WorkRequests
     * */
    override fun cancelWork() {}

    /**
     * Creates the input data bundle which includes the blur level to
     * update the amount of blur to be applied and the Uri to operate on
     * @return Data which contains the Image Uri as a String and blur level as an Integer
     */
    private fun createInputDataForWorkRequest(blurLevel: Int, imageUri: Uri): Data {
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URI, imageUri.toString()).putInt(KEY_BLUR_LEVEL, blurLevel)
        return builder.build()
    }
}
package com.fanda.happybirthday.mars.data

import com.fanda.happybirthday.mars.network.MarsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/*
* 包含应用所需的依赖项的容器对象
* */
interface AppContainer {
    val marsPhotosRepository: MarsPhotosRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com"

    private val retrofit = Retrofit.Builder().addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).baseUrl(baseUrl).build()

    private val retrofitService: MarsApiService by lazy { retrofit.create(MarsApiService::class.java) }

    override val marsPhotosRepository: MarsPhotosRepository by lazy { NetworkMarsPhotosRepository(retrofitService) }
}
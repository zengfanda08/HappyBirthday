package com.fanda.happybirthday.mars.network

import com.fanda.happybirthday.mars.model.MarsPhoto
import retrofit2.http.GET

interface MarsApiService {
    // 挂起函数，异步调用
    @GET("photos") suspend fun getPhotos(): List<MarsPhoto>
}
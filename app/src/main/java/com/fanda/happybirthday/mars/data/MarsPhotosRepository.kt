package com.fanda.happybirthday.mars.data

import com.fanda.happybirthday.mars.network.MarsApiService
import com.fanda.happybirthday.mars.model.MarsPhoto

/*
* 数据层接口
* */
interface MarsPhotosRepository {
    suspend fun getMarsPhotos(): List<MarsPhoto>
}

class NetworkMarsPhotosRepository(private val marsApiService: MarsApiService) : MarsPhotosRepository {
    override suspend fun getMarsPhotos(): List<MarsPhoto> {
        return marsApiService.getPhotos()
    }
}
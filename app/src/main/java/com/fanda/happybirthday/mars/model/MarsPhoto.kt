package com.fanda.happybirthday.mars.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 使用 @SerialName(value = "img_src") 将该变量映射到 JSON 属性 img_src
@Serializable data class MarsPhoto(val id: String, @SerialName(value = "img_src") val imgSrc: String)

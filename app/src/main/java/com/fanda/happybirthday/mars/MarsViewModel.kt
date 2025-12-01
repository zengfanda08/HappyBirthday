package com.fanda.happybirthday.mars

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fanda.happybirthday.MarsPhotosApplication
import com.fanda.happybirthday.mars.data.MarsPhotosRepository
import com.fanda.happybirthday.mars.data.NetworkMarsPhotosRepository
import com.fanda.happybirthday.mars.model.MarsPhoto
import kotlinx.coroutines.launch

// 状态密封接口
sealed interface MarsUiState {
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    object Loading : MarsUiState
    object Error : MarsUiState
}

class MarsViewModel(private val marsPhotosRepository: MarsPhotosRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getMarsPhotos() {
        viewModelScope.launch {
            marsUiState = try {
                // 仓库将提供数据，而不是由 ViewModel 直接发出关于数据的网络请求
                MarsUiState.Success(marsPhotosRepository.getMarsPhotos())
            } catch (e: Exception) {
                // 捕获异常，不然会崩溃
                MarsUiState.Error
            }
        }
    }

    // 由于 Android 框架不允许在创建时向构造函数中的 ViewModel 传递值，因此我们实现了一个 ViewModelProvider.Factory 对象来绕过此限制。
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                //  APPLICATION_KEY 用于查找应用的 MarsPhotosApplication 对象
                val app = (this[APPLICATION_KEY] as MarsPhotosApplication)
                MarsViewModel(app.appContainer.marsPhotosRepository)
            }
        }
    }
}
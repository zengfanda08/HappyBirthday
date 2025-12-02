package com.fanda.happybirthday.store.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fanda.happybirthday.R
import com.fanda.happybirthday.store.data.DessertReleaseApplication
import com.fanda.happybirthday.store.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
 * View model of Dessert Release components
 */
class DessertReleaseViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    // UI states access for various [DessertReleaseUiState]
    val uiState: StateFlow<DessertReleaseUiState> = userPreferencesRepository.isLinearLayout.map {
        DessertReleaseUiState(it)
    }.stateIn(
        // 注意初始化值的赋值方式
        // runBlocking 会阻塞，最好用 加载状态，这样 UI 可以先显示 loading，再显示真实数据，且 完全非阻塞
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = runBlocking {
            DessertReleaseUiState(userPreferencesRepository.isLinearLayout.first())
        })

    /*
     * [selectLayout] change the layout and icons accordingly and
     * save the selection in DataStore through [userPreferencesRepository]
     */
    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DessertReleaseApplication)
                DessertReleaseViewModel(application.userPreferencesRepository)
            }
        }
    }
}

/*
 * Data class containing various UI States for Dessert Release screens
 */
data class DessertReleaseUiState(
    val isLinearLayout: Boolean = true,
    val toggleContentDescription: Int = if (isLinearLayout) R.string.grid_layout_toggle else R.string.linear_layout_toggle,
    val toggleIcon: Int = if (isLinearLayout) R.drawable.ic_grid_layout else R.drawable.ic_linear_layout
)
package com.farrasmuhammadrazan0100.assement2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDao
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDb
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity
import com.farrasmuhammadrazan0100.assement2.util.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    val dao: ManhwaDao,
    private val dataStore: SettingsDataStore
) : ViewModel() {

    val data: StateFlow<List<ManhwaEntity>> = dao.getAllManhwa().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    val isList: StateFlow<Boolean> = dataStore.layoutFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = true
    )

    val isDarkMode: StateFlow<Boolean> = dataStore.darkModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val dao = ManhwaDb.getInstance(application).manhwaDao
                val dataStore = SettingsDataStore(application)

                return MainViewModel(dao, dataStore) as T
            }
        }
    }
}
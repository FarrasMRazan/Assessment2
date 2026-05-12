package com.farrasmuhammadrazan0100.assement2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDao
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity
import com.farrasmuhammadrazan0100.assement2.util.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val dao: ManhwaDao,
    private val prefs: SettingsDataStore
) : ViewModel() {

    val allManhwa: StateFlow<List<ManhwaEntity>> = dao.getAllManhwa().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val isListMode: StateFlow<Boolean> = prefs.layoutFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    val isDarkMode: StateFlow<Boolean> = prefs.darkModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun insertManhwa(manhwa: ManhwaEntity) {
        viewModelScope.launch {
            dao.insertManhwa(manhwa)
        }
    }

    fun updateLayout(isList: Boolean) {
        viewModelScope.launch {
            prefs.saveLayout(isList)
        }
    }

    fun updateTheme(isDark: Boolean) {
        viewModelScope.launch {
            prefs.saveDarkMode(isDark)
        }
    }
}

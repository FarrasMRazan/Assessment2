package com.farrasmuhammadrazan0100.assement2.ui.screen // Sesuaikan package

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDao
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: ManhwaDao) : ViewModel() {
    val data: StateFlow<List<ManhwaEntity>> = dao.getAllManhwa().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}
package com.farrasmuhammadrazan0100.assement2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDao
import com.farrasmuhammadrazan0100.assement2.ui.screen.MainViewModel

class ViewModelFactory(
    private val dao: ManhwaDao,
    private val dataStore: SettingsDataStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao, dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
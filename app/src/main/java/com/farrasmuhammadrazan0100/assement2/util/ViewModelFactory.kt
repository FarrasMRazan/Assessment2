package com.farrasmuhammadrazan0100.assement2.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDb
import com.farrasmuhammadrazan0100.assement2.ui.screen.MainViewModel

class ViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val dao = ManhwaDb.getInstance(context).manhwaDao
            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
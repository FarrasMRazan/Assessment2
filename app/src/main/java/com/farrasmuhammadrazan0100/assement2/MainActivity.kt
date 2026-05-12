package com.farrasmuhammadrazan0100.assement2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.farrasmuhammadrazan0100.assement2.navigation.SetupNavGraph
import com.farrasmuhammadrazan0100.assement2.ui.theme.Assement2Theme
import com.farrasmuhammadrazan0100.assement2.util.SettingsDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = SettingsDataStore(this)

        setContent {
            val isDarkMode by dataStore.darkModeFlow.collectAsState(initial = false)

            Assement2Theme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Cukup panggil SetupNavGraph, tidak perlu kirim viewModel ke tema
                    SetupNavGraph()
                }
            }
        }
    }
}

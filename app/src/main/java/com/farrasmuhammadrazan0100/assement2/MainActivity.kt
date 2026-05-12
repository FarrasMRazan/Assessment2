package com.farrasmuhammadrazan0100.assement2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.farrasmuhammadrazan0100.assement2.navigation.SetupNavGraph
import com.farrasmuhammadrazan0100.assement2.ui.screen.MainViewModel
import com.farrasmuhammadrazan0100.assement2.ui.theme.Assement2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)
            val isDarkMode by viewModel.isDarkMode.collectAsState(initial = false)
            Assement2Theme(darkTheme = isDarkMode) {
                SetupNavGraph()
            }
        }
    }
}

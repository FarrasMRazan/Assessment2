package com.farrasmuhammadrazan0100.assement2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDb
import com.farrasmuhammadrazan0100.assement2.navigation.NavGraph
import com.farrasmuhammadrazan0100.assement2.util.SettingsDataStore
import com.farrasmuhammadrazan0100.assement2.util.ViewModelFactory
import com.farrasmuhammadrazan0100.assement2.ui.screen.MainViewModel
import com.farrasmuhammadrazan0100.assement2.ui.theme.Assement2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val database = ManhwaDb.getDatabase(context)
            val dao = database.manhwaDao
            val prefs = SettingsDataStore(context)

            val viewModel: MainViewModel = viewModel(
                factory = ViewModelFactory(dao, prefs)
            )

            val isDarkMode by viewModel.isDarkMode.collectAsState()

            Assement2Theme(darkTheme = isDarkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
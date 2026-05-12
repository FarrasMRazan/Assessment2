package com.farrasmuhammadrazan0100.assement2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDb
import com.farrasmuhammadrazan0100.assement2.ui.screen.DetailScreen
import com.farrasmuhammadrazan0100.assement2.ui.screen.MainScreen
import com.farrasmuhammadrazan0100.assement2.ui.screen.MainViewModel
import com.farrasmuhammadrazan0100.assement2.util.SettingsDataStore
import com.farrasmuhammadrazan0100.assement2.util.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = ManhwaDb.getInstance(context)
    val dao = db.manhwaDao
    val characterDao = db.characterDao          // tambahan
    val dataStore = SettingsDataStore(context)

    val viewModel: MainViewModel = viewModel(
        factory = ViewModelFactory(dao, characterDao, dataStore)   // update factory
    )

    val isList by dataStore.layoutFlow.collectAsState(initial = true)
    val isDarkMode by dataStore.darkModeFlow.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(
                navController = navController,
                viewModel = viewModel,
                isList = isList,
                isDarkMode = isDarkMode,
                onLayoutChange = { scope.launch { dataStore.saveLayout(it) } },
                onThemeChange = { scope.launch { dataStore.saveDarkMode(it) } }
            )
        }

        composable(route = Screen.FormBaru.route) {
            DetailScreen(navController, dao)
        }

        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_MANHWA) { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt(KEY_ID_MANHWA)
            DetailScreen(navController, dao, id)
        }
    }
}
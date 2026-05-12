package com.farrasmuhammadrazan0100.assement2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.farrasmuhammadrazan0100.assement2.ui.screen.DetailScreen
import com.farrasmuhammadrazan0100.assement2.ui.screen.MainScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }

        composable(route = Screen.FormBaru.route) {
            DetailScreen(navController)
        }

        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_MANHWA) { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt(KEY_ID_MANHWA)
            DetailScreen(navController, id)
        }
    }
}
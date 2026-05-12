package com.farrasmuhammadrazan0100.assement2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.farrasmuhammadrazan0100.assement2.ui.screen.MainScreen
import com.farrasmuhammadrazan0100.assement2.ui.screen.DetailScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("manhwaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("manhwaId") ?: -1
            DetailScreen(
                manhwaId = id,
                onBack = { navController.navigateUp() }
            )
        }
    }
}
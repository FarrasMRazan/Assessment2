package com.farrasmuhammadrazan0100.assement2.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{manhwaId}") {
        fun createRoute(manhwaId: Int) = "detail/$manhwaId"
    }
}
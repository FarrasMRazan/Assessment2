package com.farrasmuhammadrazan0100.assement2.navigation
const val KEY_ID_MANHWA = "manhwaID"
sealed class Screen(val route: String) {
    object Home : Screen("mainScreen")
    object FormBaru : Screen("detailScreen")
    object FormUbah : Screen("detailScreen/{$KEY_ID_MANHWA}") {
        fun withId(id: Int) = "detailScreen/$id"
    }
}

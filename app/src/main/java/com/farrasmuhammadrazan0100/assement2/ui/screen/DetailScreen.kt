package com.farrasmuhammadrazan0100.assement2.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen(manhwaId: Int, onBack: () -> Unit) {
    Text(text = "Detail Manhwa ID: $manhwaId")
}
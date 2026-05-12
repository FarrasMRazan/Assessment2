package com.farrasmuhammadrazan0100.assement2.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.farrasmuhammadrazan0100.assement2.R
import com.farrasmuhammadrazan0100.assement2.navigation.Screen
import com.farrasmuhammadrazan0100.assement2.ui.screen.component.ManhwaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    isList: Boolean,         // State dari DataStore (Layout)
    isDarkMode: Boolean,     // State dari DataStore (Tema)
    onLayoutChange: (Boolean) -> Unit, // Callback untuk ubah layout
    onThemeChange: (Boolean) -> Unit   // Callback untuk ubah tema
) {
    // Mengambil data Manhwa dari Room melalui ViewModel
    val data by viewModel.data.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ManhwaVault") },
                actions = {
                    // Fitur Toggle List/Grid (Poin 5 Rubrik)
                    IconButton(onClick = { onLayoutChange(!isList) }) {
                        Icon(
                            painter = painterResource(
                                id = if (isList) R.drawable.ic_grid_view else R.drawable.ic_list_view
                            ),
                            contentDescription = "Ganti Layout"
                        )
                    }
                    // Fitur Toggle Dark/Light Mode (Poin 5 Rubrik)
                    IconButton(onClick = { onThemeChange(!isDarkMode) }) {
                        Icon(
                            painter = painterResource(
                                id = if (isDarkMode) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
                            ),
                            contentDescription = "Ganti Tema"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
        floatingActionButton = {
            // Navigasi ke Form Tambah (Poin 6 Rubrik)
            FloatingActionButton(onClick = {
                navController.navigate(Screen.FormBaru.route)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Manhwa")
            }
        }
    ) { padding ->
        if (data.isEmpty()) {
            // Tampilan jika database kosong (Empty State - Poin 1 Rubrik)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_empty_state),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Belum ada koleksi Manhwa.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // Menampilkan data (Urut otomatis berdasarkan judul dari DAO)
            if (isList) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding(),
                        bottom = 80.dp // Agar item terakhir tidak tertutup FAB (Poin 1)
                    )
                ) {
                    items(data) { manhwa ->
                        ManhwaItem(
                            manhwa = manhwa,
                            isGrid = false,
                            onClick = {
                                navController.navigate(Screen.FormUbah.withId(manhwa.id))
                            }
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding(),
                        bottom = 80.dp // Poin 1 Rubrik
                    )
                ) {
                    items(data) { manhwa ->
                        ManhwaItem(
                            manhwa = manhwa,
                            isGrid = true,
                            onClick = {
                                navController.navigate(Screen.FormUbah.withId(manhwa.id))
                            }
                        )
                    }
                }
            }
        }
    }
}
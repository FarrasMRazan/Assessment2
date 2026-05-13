package com.farrasmuhammadrazan0100.assement2.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.farrasmuhammadrazan0100.assement2.R
import com.farrasmuhammadrazan0100.assement2.navigation.Screen
import com.farrasmuhammadrazan0100.assement2.ui.screen.component.AddCharacterDialog
import com.farrasmuhammadrazan0100.assement2.ui.screen.component.CharacterItem
import com.farrasmuhammadrazan0100.assement2.ui.screen.component.ManhwaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    isList: Boolean,
    isDarkMode: Boolean,
    onLayoutChange: (Boolean) -> Unit,
    onThemeChange: (Boolean) -> Unit
) {
    val data by viewModel.data.collectAsState()
    val characters by viewModel.characters.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddCharacterDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(R.string.tab_manhwa),
        stringResource(R.string.tab_character)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_title)) },
                actions = {
                    IconButton(onClick = { onLayoutChange(!isList) }) {
                        Icon(
                            painter = painterResource(
                                id = if (isList) R.drawable.ic_grid_view else R.drawable.ic_list_view
                            ),
                            contentDescription = stringResource(R.string.change_layout)
                        )
                    }
                    IconButton(onClick = { onThemeChange(!isDarkMode) }) {
                        Icon(
                            painter = painterResource(
                                id = if (isDarkMode) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
                            ),
                            contentDescription = stringResource(R.string.change_theme)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (selectedTab == 0) {
                    navController.navigate(Screen.FormBaru.route)
                } else {
                    showAddCharacterDialog = true
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(
                        if (selectedTab == 0) R.string.add_manhwa else R.string.add_character
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab Row
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> ManhwaTabContent(
                    data = data,
                    isList = isList,
                    onItemClick = { manhwaId ->
                        navController.navigate(Screen.FormUbah.withId(manhwaId))
                    }
                )
                1 -> CharacterTabContent(
                    characters = characters,
                    manhwaList = data,
                    onDeleteCharacter = { character ->
                        viewModel.deleteCharacter(character)
                    }
                )
            }
        }
    }

    // Dialog Tambah Character
    if (showAddCharacterDialog) {
        AddCharacterDialog(
            manhwaList = data,
            onDismiss = { showAddCharacterDialog = false },
            onConfirm = { manhwaId, charName, charRole ->
                viewModel.insertCharacter(manhwaId, charName, charRole)
                showAddCharacterDialog = false
            }
        )
    }
}

@Composable
private fun ManhwaTabContent(
    data: List<com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity>,
    isList: Boolean,
    onItemClick: (Int) -> Unit
) {
    if (data.isEmpty()) {
        EmptyState(message = stringResource(R.string.empty_manhwa))
    } else {
        if (isList) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data) { manhwa ->
                    ManhwaItem(
                        manhwa = manhwa,
                        isGrid = false,
                        onClick = { onItemClick(manhwa.id) }
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data) { manhwa ->
                    ManhwaItem(
                        manhwa = manhwa,
                        isGrid = true,
                        onClick = { onItemClick(manhwa.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterTabContent(
    characters: List<com.farrasmuhammadrazan0100.assement2.model.CharacterEntity>,
    manhwaList: List<com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity>,
    onDeleteCharacter: (com.farrasmuhammadrazan0100.assement2.model.CharacterEntity) -> Unit
) {
    if (characters.isEmpty()) {
        EmptyState(message = stringResource(R.string.empty_character))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(characters) { character ->
                val manhwaTitle = manhwaList.find { it.id == character.manhwaId }?.title ?: "Unknown"
                CharacterItem(
                    character = character,
                    manhwaTitle = manhwaTitle,
                    onDelete = { onDeleteCharacter(character) }
                )
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_state),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
    }
}
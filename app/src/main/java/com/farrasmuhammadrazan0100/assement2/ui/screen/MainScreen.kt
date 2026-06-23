package com.farrasmuhammadrazan0100.assement2.ui.screen

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.farrasmuhammadrazan0100.assement2.R
import com.farrasmuhammadrazan0100.assement2.model.CharacterEntity
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity
import com.farrasmuhammadrazan0100.assement2.navigation.Screen
import com.farrasmuhammadrazan0100.assement2.ui.screen.component.AddCharacterDialog
import com.farrasmuhammadrazan0100.assement2.ui.screen.component.CharacterItem
import com.farrasmuhammadrazan0100.assement2.ui.screen.component.ManhwaDialog
import com.farrasmuhammadrazan0100.assement2.ui.screen.component.ManhwaItem

private fun getCroppedImage(
    resolver: android.content.ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("MANHWA", "getCroppedImage Error: ${result.error}")
        return null
    }
    val uri = result.uriContent ?: return null
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    isList: Boolean,
    isDarkMode: Boolean,
    onLayoutChange: (Boolean) -> Unit,
    onThemeChange: (Boolean) -> Unit,
    userId: String
) {
    val context = LocalContext.current
    val data by viewModel.data.collectAsState()
    val characters by viewModel.characters.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddCharacterDialog by remember { mutableStateOf(false) }
    // State baru: tampilkan ManhwaDialog setelah bitmap dari kamera tersedia
    var showManhwaDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        stringResource(R.string.tab_manhwa),
        stringResource(R.string.tab_character)
    )

    //LaunchedEffect(userId) {
      //  viewModel.setUserId(userId)
    //}

    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        // Tampilkan dialog hanya jika bitmap berhasil didapat
        if (bitmap != null) showManhwaDialog = true
    }

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
                    val options = CropImageContractOptions(
                        null,
                        CropImageOptions(
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)
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
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            when (selectedTab) {
                0 -> ManhwaTabContent(data = data, isList = isList,
                    onItemClick = { navController.navigate(Screen.FormUbah.withId(it)) })
                1 -> CharacterTabContent(characters = characters, manhwaList = data,
                    onDeleteCharacter = { viewModel.deleteCharacter(it) })
            }
        }
    }

    // Dialog tambah manhwa (setelah foto dari kamera)
    // Saat simpan diklik → Log.d dulu (Task 3.2), nanti diganti kirim ke server (Task 3.3)
    if (showManhwaDialog) {
        ManhwaDialog(
            bitmap = bitmap,
            onDismissRequest = { showManhwaDialog = false },
            onConfirmation = { judul, author, rating ->
                Log.d("TAMBAH", "$judul $author ditambahkan.")
                showManhwaDialog = false
                // TODO Task 3.3: ganti Log.d di atas dengan viewModel.saveManhwa(...)
            }
        )
    }

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
    data: List<ManhwaEntity>, isList: Boolean, onItemClick: (Int) -> Unit
) {
    if (data.isEmpty()) {
        EmptyState(message = stringResource(R.string.empty_manhwa))
    } else {
        if (isList) {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
                items(data) { ManhwaItem(manhwa = it, isGrid = false, onClick = { onItemClick(it.id) }) }
            }
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)) {
                items(data) { ManhwaItem(manhwa = it, isGrid = true, onClick = { onItemClick(it.id) }) }
            }
        }
    }
}

@Composable
private fun CharacterTabContent(
    characters: List<CharacterEntity>,
    manhwaList: List<ManhwaEntity>,
    onDeleteCharacter: (CharacterEntity) -> Unit
) {
    if (characters.isEmpty()) {
        EmptyState(message = stringResource(R.string.empty_character))
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
            items(characters) { character ->
                val title = manhwaList.find { it.id == character.manhwaId }?.title ?: "Unknown"
                CharacterItem(character = character, manhwaTitle = title,
                    onDelete = { onDeleteCharacter(character) })
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_empty_state),
            contentDescription = null, modifier = Modifier.size(120.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
    }
}
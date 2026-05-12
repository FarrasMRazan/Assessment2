package com.farrasmuhammadrazan0100.assement2.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDao
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity
import com.farrasmuhammadrazan0100.assement2.ui.component.DeleteConfirmDialog
import kotlinx.coroutines.launch
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    dao: ManhwaDao,
    id: Int? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { imageUri = it.toString() }
    }

    var showDialog by remember { mutableStateOf(false) }
    var expandedMenu by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        if (id != null) {
            dao.getAllManhwa().collect { list ->
                val manhwa = list.find { it.id == id }
                manhwa?.let {
                    title = it.title
                    author = it.author
                    rating = it.rating.toString()
                    imageUri = it.imageUri
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (id == null) "Tambah Manhwa" else "Ubah Manhwa") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                actions = {
                    if (id != null) {
                        IconButton(onClick = { expandedMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Opsi")
                        }
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Hapus Manhwa") },
                                onClick = {
                                    expandedMenu = false
                                    showDialog = true
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Manhwa") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Penulis") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rating,
                onValueChange = { rating = it },
                label = { Text("Rating (Contoh: 4.5)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            if (imageUri.isNotBlank()) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RectangleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (imageUri.isBlank()) "Pilih Gambar dari Galeri" else "Ganti Gambar")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.isBlank() || author.isBlank() || rating.isBlank()) {
                        Toast.makeText(context, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    } else {
                        val manhwa = ManhwaEntity(
                            id = id ?: 0, // Jika null, Room akan auto-generate ID karena autoGenerate=true
                            title = title,
                            author = author,
                            rating = rating.toFloatOrNull() ?: 0f,
                            imageUri = imageUri
                        )

                        scope.launch {
                            if (id == null) dao.insertManhwa(manhwa) else dao.updateManhwa(manhwa)
                            Toast.makeText(context, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (id == null) "Simpan Data" else "Perbarui Data")
            }
        }
    }

    // Pemanggilan Dialog Konfirmasi Hapus yang dipisah (Poin 4)
    if (showDialog) {
        DeleteConfirmDialog(
            onDismissRequest = { showDialog = false },
            onConfirm = {
                scope.launch {
                    id?.let {
                        val dummy = ManhwaEntity(id = it, title="", author="", rating=0f, imageUri="")
                        dao.deleteManhwa(dummy)
                        Toast.makeText(context, "Data telah dihapus", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
                showDialog = false
            }
        )
    }
}
package com.farrasmuhammadrazan0100.assement2.ui.screen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.farrasmuhammadrazan0100.assement2.database.CharacterDao
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDao
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDb
import com.farrasmuhammadrazan0100.assement2.model.CharacterEntity
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity
import com.farrasmuhammadrazan0100.assement2.network.ManhwaApiConfig
import com.farrasmuhammadrazan0100.assement2.util.SettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel(
    val dao: ManhwaDao,
    private val characterDao: CharacterDao,
    private val dataStore: SettingsDataStore
) : ViewModel() {

    private val _userId = MutableStateFlow("")

    val data: StateFlow<List<ManhwaEntity>> = _userId.flatMapLatest { uid ->
        dao.getManhwaByUser(uid)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    val characters: StateFlow<List<CharacterEntity>> = data.flatMapLatest { manhwaList ->
        if (manhwaList.isEmpty()) flowOf(emptyList())
        else combine(manhwaList.map { characterDao.getCharactersByManhwa(it.id) }) { arrays ->
            arrays.flatMap { it.toList() }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    val isList: StateFlow<Boolean> = dataStore.layoutFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000L), initialValue = true
    )

    val isDarkMode: StateFlow<Boolean> = dataStore.darkModeFlow.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000L), initialValue = false
    )

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun setUserId(userId: String) {
        _userId.value = userId
    }

    fun clearMessage() {
        _errorMessage.value = null
    }

    fun saveLayout(isList: Boolean) {
        viewModelScope.launch { dataStore.saveLayout(isList) }
    }

    fun saveDarkMode(isDark: Boolean) {
        viewModelScope.launch { dataStore.saveDarkMode(isDark) }
    }

    fun updateManhwa(manhwa: ManhwaEntity) {
        viewModelScope.launch { dao.updateManhwa(manhwa) }
    }

    fun deleteManhwa(manhwa: ManhwaEntity) {
        viewModelScope.launch { dao.deleteManhwa(manhwa) }
    }

    fun insertCharacter(manhwaId: Int, charName: String, charRole: String) {
        viewModelScope.launch {
            characterDao.insertCharacter(
                CharacterEntity(manhwaId = manhwaId, charName = charName, charRole = charRole)
            )
        }
    }

    fun deleteCharacter(character: CharacterEntity) {
        viewModelScope.launch { characterDao.deleteCharacter(character) }
    }

    fun saveManhwa(
        userId: String,
        judul: String,
        author: String,
        rating: Float,
        bitmap: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val imagePart = bitmap.toMultipartBody()
                val judulBody = judul.toRequestBody("text/plain".toMediaTypeOrNull())
                val authorBody = author.toRequestBody("text/plain".toMediaTypeOrNull())
                val ratingBody = rating.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                try {
                    val result = ManhwaApiConfig.collectionService.postManhwa(
                        userId = userId,
                        judul = judulBody,
                        author = authorBody,
                        rating = ratingBody,
                        image = imagePart
                    )
                    if (result.status != "success") {
                        throw Exception(result.message)
                    }
                } catch (serverEx: Exception) {
                }

                dao.insertManhwa(
                    ManhwaEntity(
                        id = 0,
                        title = judul,
                        author = author,
                        rating = rating,
                        imageUri = "",
                        userId = userId
                    )
                )

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRandomRecommendation(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val response = ManhwaApiConfig.shiroService.getRandomManhwa()
                saveShiroResult(response.name, response.category.name, userId)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mengambil rekomendasi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRecommendationByCategory(category: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val response = ManhwaApiConfig.shiroService.getManhwaByCategory(category)
                saveShiroResult(response.name, response.category.name, userId)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mengambil rekomendasi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun saveShiroResult(name: String, categoryName: String, userId: String) {
        val title = name.split(" ")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

        val existing = data.value.find { it.title.equals(title, ignoreCase = true) }
        if (existing != null) {
            _errorMessage.value = "\"$title\" sudah ada di koleksi kamu!"
            return
        }

        dao.insertManhwa(
            ManhwaEntity(
                id = 0,
                title = title,
                author = "Shiro API · $categoryName",
                rating = 0f,
                imageUri = "",
                userId = ""
            )
        )
        _errorMessage.value = "✓ \"$title\" ($categoryName) ditambahkan!"
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val db = ManhwaDb.getInstance(application)
                val dataStore = SettingsDataStore(application)
                return MainViewModel(db.manhwaDao, db.characterDao, dataStore) as T
            }
        }
    }
}
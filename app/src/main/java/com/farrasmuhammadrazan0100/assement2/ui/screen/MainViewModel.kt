package com.farrasmuhammadrazan0100.assement2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.farrasmuhammadrazan0100.assement2.database.CharacterDao
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDao
import com.farrasmuhammadrazan0100.assement2.database.ManhwaDb
import com.farrasmuhammadrazan0100.assement2.model.CharacterEntity
import com.farrasmuhammadrazan0100.assement2.model.ManhwaEntity
import com.farrasmuhammadrazan0100.assement2.util.SettingsDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    val dao: ManhwaDao,
    private val characterDao: CharacterDao,
    private val dataStore: SettingsDataStore
) : ViewModel() {

    // Semua manhwa
    val data: StateFlow<List<ManhwaEntity>> = dao.getAllManhwa().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    // Semua karakter dari semua manhwa (flat list)
    // Menggunakan flatMapLatest agar reaktif ketika manhwa berubah
    val characters: StateFlow<List<CharacterEntity>> = data.flatMapLatest { manhwaList ->
        if (manhwaList.isEmpty()) {
            flowOf(emptyList())
        } else {
            // Combine flow dari setiap manhwa menjadi satu list
            combine(
                manhwaList.map { manhwa ->
                    characterDao.getCharactersByManhwa(manhwa.id)
                }
            ) { arrays ->
                arrays.flatMap { it.toList() }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    val isList: StateFlow<Boolean> = dataStore.layoutFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = true
    )

    val isDarkMode: StateFlow<Boolean> = dataStore.darkModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    fun insertCharacter(manhwaId: Int, charName: String, charRole: String) {
        viewModelScope.launch {
            characterDao.insertCharacter(
                CharacterEntity(
                    manhwaId = manhwaId,
                    charName = charName,
                    charRole = charRole
                )
            )
        }
    }

    fun deleteCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            characterDao.deleteCharacter(character)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val db = ManhwaDb.getInstance(application)
                val dataStore = SettingsDataStore(application)
                return MainViewModel(db.manhwaDao, db.characterDao, dataStore) as T
            }
        }
    }
}
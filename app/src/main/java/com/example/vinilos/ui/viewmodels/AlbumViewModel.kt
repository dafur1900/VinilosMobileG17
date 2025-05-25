package com.example.vinilos.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.models.Comment
import com.example.vinilos.data.repositories.AlbumRepository
import com.example.vinilos.data.repositories.BandRepository
import com.example.vinilos.data.repositories.MusicianRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val albumRepository = AlbumRepository(application)

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    private val _isTracksEmpty = MutableLiveData<Boolean>()
    val isTracksEmpty: LiveData<Boolean> get() = _isTracksEmpty

    // Añadiendo variable para los comentarios
    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        albumRepository.refreshData({ albumList ->
            _albums.postValue(albumList)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        }, {
            if (_albums.value.isNullOrEmpty()) {
                _eventNetworkError.value = true
            }
        })
    }

    // Método para cargar comentarios de un álbum específico
    fun loadComments(albumId: Int) {
        viewModelScope.launch {
            try {
                val album = _albums.value?.find { it.id == albumId }
                album?.let {
                    _comments.value = it.comments
                }
            } catch (e: Exception) {
                Log.e("AlbumViewModel", "Error loading comments: ${e.message}")
            }
        }
    }

    // Método para publicar un nuevo comentario
    suspend fun postComment(albumId: Int, commentText: String, rating: Int) {
        try {
            // Aquí normalmente harías una llamada a tu repositorio para enviar el comentario
            // Como no tenemos implementación real, simulamos añadiendo el comentario a la lista actual
            val newComment = Comment(
                id = System.currentTimeMillis().toInt(),
                description = commentText,
                rating = rating,
                collector = 1 // ID del coleccionista actual
            )
            
            val currentComments = _comments.value?.toMutableList() ?: mutableListOf()
            currentComments.add(0, newComment)
            _comments.postValue(currentComments)
            
            // También actualizaríamos el álbum actual con el nuevo comentario
            val currentAlbum = _albums.value?.find { it.id == albumId }
            currentAlbum?.let { album ->
                val updatedAlbum = album.copy(comments = currentComments)
                val updatedAlbums = _albums.value?.map { if (it.id == albumId) updatedAlbum else it }
                _albums.postValue(updatedAlbums)
            }
        } catch (e: Exception) {
            Log.e("AlbumViewModel", "Error posting comment: ${e.message}")
            throw e
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun getAlbumById(id: Int): LiveData<Album?> {
        val result = MediatorLiveData<Album?>()
        result.addSource(_albums) { albums ->
            result.value = albums?.find { it.id == id }
        }
        return result
    }

    fun setAlbum(album: Album) {
        _isTracksEmpty.value = album.tracks.isEmpty()
    }

    fun formatDateAndGenre(dateString: String?, genre: String?): String {
        return try {

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("MMMM dd 'de' yyyy", Locale("es", "CO"))

            val date = dateString?.let { inputFormat.parse(it) }
            val formattedDate = outputFormat.format(date!!)

            val formattedDateWithCapitalMonth = formattedDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            "$formattedDateWithCapitalMonth - ${genre ?: ""}"
        } catch (e: Exception) {
            "Fecha inválida - ${genre ?: ""}"
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}

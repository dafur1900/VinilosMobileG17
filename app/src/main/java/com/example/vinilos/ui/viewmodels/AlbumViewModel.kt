package com.example.vinilos.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.repositories.AlbumRepository
import com.example.vinilos.data.repositories.BandRepository
import com.example.vinilos.data.repositories.MusicianRepository

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val albumRepository = AlbumRepository(application)

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    init {
        loadAlbums()
    }


    fun loadAlbums() {
        albumRepository.refreshData({
            _albums.postValue(it)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        }, {
            _eventNetworkError.value = true
        })
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
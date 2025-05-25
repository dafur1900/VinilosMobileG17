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
import com.example.vinilos.data.repositories.BandRepository
import com.example.vinilos.data.repositories.MusicianRepository
import com.example.vinilos.data.network.NetworkServiceAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ArtistViewModel(application: Application) : AndroidViewModel(application) {

    private val bandRepository = BandRepository(application)

    private val musicianRepository = MusicianRepository(application)

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> get() = _artists

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)

    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    init {
        loadArtists(ArtistType.MUSICIAN)
    }


    fun loadArtists(type: ArtistType) {
        try {
            when (type) {
                ArtistType.MUSICIAN -> musicianRepository.refreshData(
                    callback = { artists ->
                        _artists.postValue(artists)
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                    },
                    onError = {
                        _eventNetworkError.postValue(true)
                        _isNetworkErrorShown.postValue(false)
                    }
                )
                ArtistType.BAND -> bandRepository.refreshData(
                    callback = { bands ->
                        _artists.postValue(bands)
                        _eventNetworkError.postValue(false)
                        _isNetworkErrorShown.postValue(false)
                    },
                    onError = {
                        _eventNetworkError.postValue(true)
                        _isNetworkErrorShown.postValue(false)
                    }
                )
            }
        } catch (e: Exception) {
            _eventNetworkError.postValue(true)
            _isNetworkErrorShown.postValue(false)
        }
    }

    enum class ArtistType {
        MUSICIAN, BAND
    }


    fun getArtistById(id: Int, type: ArtistType): LiveData<Artist?> {
        val result = MediatorLiveData<Artist?>()

        viewModelScope.launch {
            try {
                val artist = NetworkServiceAdapter.getInstance(getApplication()).getArtistById(id, type == ArtistType.MUSICIAN)
                result.postValue(artist)
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            } catch (e: Exception) {
                Log.e("ArtistViewModel", "Error loading artist: ${e.message}")
                _eventNetworkError.postValue(true)
                _isNetworkErrorShown.postValue(false)
            }
        }

        return result
    }

    fun formatDate(dateString: String?): String {
        Log.d("DateFormat", "Received date: $dateString")

        return try {

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("MMMM dd 'de' yyyy", Locale("es", "CO"))

            val date = dateString?.let { inputFormat.parse(it) }
            val formattedDate = outputFormat.format(date!!)

            val formattedDateWithCapitalMonth = formattedDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            formattedDateWithCapitalMonth
        } catch (e: Exception) {

            "Fecha inválida"
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
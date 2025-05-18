package com.example.vinilos.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.VolleyError
import com.example.vinilos.data.models.Collector
import com.example.vinilos.data.repositories.CollectorRepository
import kotlinx.coroutines.launch

class CollectorViewModel(application: Application) : AndroidViewModel(application) {

    private val collectorRepository = CollectorRepository(application)

    private val _collectors = MutableLiveData<List<Collector>>()
    val collectors: LiveData<List<Collector>>
        get() = _collectors


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    private val _error = MutableLiveData<VolleyError?>()
    val error: LiveData<VolleyError?>
        get() = _error

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String>
        get() = _searchQuery


    init {
        fetchCollectors(forceRefresh = false)
    }


    fun fetchCollectors(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null // Clear previous errors
            try {
                collectorRepository.getCollectors(
                    onComplete = { collectorList ->
                        _collectors.postValue(collectorList) // Use postValue if on a background thread, though getCollectors might call back on main
                        _isLoading.postValue(false)
                    },
                    onError = { volleyError ->
                        _error.postValue(volleyError)
                        _isLoading.postValue(false)
                        _collectors.postValue(emptyList()) // Clear list on error or show previous
                    },
                    forceRefresh = forceRefresh
                )
            } catch (e: Exception) {
                _error.postValue(VolleyError("An unexpected error occurred: ${e.localizedMessage}"))
                _isLoading.postValue(false)
                _collectors.postValue(emptyList())
            }
        }
    }

    fun refreshCollectorsData() {
        fetchCollectors(forceRefresh = true)
    }


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun onClearedError() {
        _error.value = null
    }
}

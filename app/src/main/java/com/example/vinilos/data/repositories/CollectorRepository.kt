package com.example.vinilos.data.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinilos.data.models.Collector
import com.example.vinilos.data.network.NetworkServiceAdapter

class CollectorRepository(private val application: Application) {

    private var collectorsCache: List<Collector>? = null

    suspend fun getCollectors(
        onComplete: (List<Collector>) -> Unit,
        onError: (VolleyError) -> Unit,
        forceRefresh: Boolean = false
    ) {
        if (!forceRefresh && collectorsCache != null) {
            collectorsCache?.let {
                onComplete(it)
                return
            }
        }

        // Fetch from network
        NetworkServiceAdapter.getInstance(application).getCollectors(
            { collectors ->
                collectorsCache = collectors
                onComplete(collectors)
            },
            { error ->
                collectorsCache = null
                onError(error)
            }
        )
    }

    suspend fun refreshCollectors(
        onComplete: (List<Collector>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        getCollectors(onComplete, onError, forceRefresh = true)
    }

    suspend fun getCollectors(): List<Collector> {
        val cache = collectorsCache
        if (cache != null) {
            return cache
        }

        return try {
            val networkAdapter = NetworkServiceAdapter.getInstance(application)
            val collectors = networkAdapter.getCollectors()
            collectorsCache = collectors
            collectors
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCollectorById(id: Int): Collector {
        // Primero intenta buscarlo en caché
        val cachedCollector = collectorsCache?.find { it.id == id }
        if (cachedCollector != null) {
            return cachedCollector
        }

        // Si no lo encuentra en caché, hace la petición a la API
        return try {
            NetworkServiceAdapter.getInstance(application).getCollectorById(id)
        } catch (e: Exception) {
            throw e
        }
    }
}

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
}

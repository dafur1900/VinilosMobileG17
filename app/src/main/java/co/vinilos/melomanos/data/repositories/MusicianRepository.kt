package co.vinilos.melomanos.repositories

import android.app.Application
import com.android.volley.VolleyError
import co.vinilos.melomanos.data.models.Artist
import co.vinilos.melomanos.data.network.NetworkServiceAdapter

class MusicianRepository(val application: Application) {
    fun refreshData(callback: (List<Artist>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getMusicians({ callback(it) }, onError)
    }
}
package co.vinilos.melomanos.data.repositories

import android.app.Application
import com.android.volley.VolleyError
import co.vinilos.melomanos.models.Artist
import co.vinilos.melomanos.data.network.NetworkServiceAdapter

class MusicianRepository(val application: Application) {
    fun refreshData(callback: (List<Artist>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getMusicians({ callback(it) }, onError)
    }
}
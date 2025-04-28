package co.vinilos.melomanos.data.repositories

import android.app.Application
import com.android.volley.VolleyError
import co.vinilos.melomanos.models.Album
import co.vinilos.melomanos.data.network.NetworkServiceAdapter

class AlbumRepository (val application: Application) {
    fun refreshData(callback: (List<Album>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getAlbums({ callback(it) }, onError)
    }
}

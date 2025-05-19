package com.example.vinilos.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.models.Comment
import com.example.vinilos.data.models.Track
import com.example.vinilos.data.models.Collector
import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
    companion object {
        const val BASE_URL = "https://backvynils-production-704a.up.railway.app/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun getBands(onComplete: (resp: List<Artist>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(
            getRequest("bands",
                { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Artist>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)
                        list.add(
                            i,
                            Artist(
                                id = item.getInt("id"),
                                name = item.getString("name"),
                                image = item.getString("image"),
                                description = item.getString("description"),
                                creationDate = item.getString("creationDate"),
                                albums = emptyList(),
                                type = Artist.ArtistType.BAND
                            )
                        )
                    }
                    val sortedList = list.sortedBy { it.name }
                    onComplete(sortedList)
                },
                {
                    onError(it)
                })
        )
    }

    fun getMusicians(
        onComplete: (resp: List<Artist>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest("musicians",
                { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Artist>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)
                        list.add(
                            i,
                            Artist(
                                id = item.getInt("id"),
                                name = item.getString("name"),
                                image = item.getString("image"),
                                description = item.getString("description"),
                                creationDate = item.getString("birthDate"),
                                albums = emptyList(),
                                type = Artist.ArtistType.MUSICIAN
                            )
                        )
                    }
                    val sortedList = list.sortedBy { it.name }
                    onComplete(sortedList)
                },
                {
                    onError(it)
                })
        )
    }

    fun getAlbums(
        onComplete: (resp: List<Album>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest("albums",
                { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Album>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)

                        // Parse the tracks list
                        val tracksArray = item.getJSONArray("tracks")
                        val tracks = mutableListOf<Track>()
                        for (j in 0 until tracksArray.length()) {
                            val trackItem = tracksArray.getJSONObject(j)
                            tracks.add(
                                Track(
                                    id = trackItem.getInt("id"),
                                    name = trackItem.getString("name"),
                                    duration = trackItem.getString("duration")
                                )
                            )
                        }

                        // Parse the performers list
                        val performersArray = item.getJSONArray("performers")
                        val performers = mutableListOf<Artist>()
                        for (j in 0 until performersArray.length()) {
                            val performerItem = performersArray.getJSONObject(j)
                            performers.add(
                                Artist(
                                    id = performerItem.getInt("id"),
                                    name = performerItem.getString("name"),
                                    image = performerItem.getString("image"),
                                    description = performerItem.getString("description"),
                                    creationDate = "",
                                    albums = emptyList(),
                                    type = Artist.ArtistType.MUSICIAN
                                )
                            )
                        }

                        // Parse the comments list
                        val commentsArray = item.getJSONArray("comments")
                        val comments = mutableListOf<Comment>()
                        for (j in 0 until commentsArray.length()) {
                            val commentItem = commentsArray.getJSONObject(j)
                            comments.add(
                                Comment(
                                    id = commentItem.getInt("id"),
                                    description = commentItem.getString("description"),
                                    rating = commentItem.getInt("rating")
                                )
                            )
                        }

                        list.add(
                            i,
                            Album(
                                id = item.getInt("id"),
                                name = item.getString("name"),
                                cover = item.getString("cover"),
                                description = item.getString("description"),
                                releaseDate = item.getString("releaseDate"),
                                genre = item.getString("genre"),
                                recordLabel = item.getString("recordLabel"),
                                tracks = tracks,
                                performers = performers,
                                comments = comments
                            )
                        )
                    }
                    onComplete(list)
                },
                {
                    onError(it)
                })
        )
    }

    fun getCollectors(onComplete: (resp: List<Collector>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(
            getRequest("collectors",
                { response ->
                    try {
                        val respJsonArray = JSONArray(response)
                        val list = mutableListOf<Collector>()
                        for (i in 0 until respJsonArray.length()) {
                            val item: JSONObject = respJsonArray.getJSONObject(i)

                            // Parsear Comments
                            val commentsArray = item.optJSONArray("comments") ?: JSONArray()
                            val commentsList = mutableListOf<Comment>()
                            for (j in 0 until commentsArray.length()) {
                                val commentItem = commentsArray.getJSONObject(j)
                                commentsList.add(
                                    Comment(
                                        id = commentItem.getInt("id"),
                                        description = commentItem.getString("description"),
                                        rating = commentItem.getInt("rating")
                                    )
                                )
                            }


                            val favoritePerformersArray = item.optJSONArray("favoritePerformers") ?: JSONArray()
                            val favoritePerformersList = mutableListOf<Artist>()
                            for (j in 0 until favoritePerformersArray.length()) {
                                val performerItem = favoritePerformersArray.getJSONObject(j)
                                favoritePerformersList.add(
                                    Artist(
                                        id = performerItem.getInt("id"),
                                        name = performerItem.getString("name"),
                                        image = performerItem.optString("image"),
                                        description = performerItem.optString("description"),


                                        creationDate = performerItem.optString("creationDate", performerItem.optString("birthDate","")),
                                        albums = emptyList(), // Para evitar complejidad/recursión aquí
                                        type = if (performerItem.has("birthDate")) Artist.ArtistType.MUSICIAN else Artist.ArtistType.BAND // Intento de inferir tipo
                                    )
                                )
                            }


                            val collectorAlbumsArray = item.optJSONArray("collectorAlbums") ?: JSONArray()
                            val collectorAlbumsList = mutableListOf<Album>()
                            for (j in 0 until collectorAlbumsArray.length()) {
                                val albumItem = collectorAlbumsArray.getJSONObject(j)

                                val tracksArray = albumItem.optJSONArray("tracks") ?: JSONArray()
                                val tracks = mutableListOf<Track>()
                                for (k in 0 until tracksArray.length()) {
                                    val trackItem = tracksArray.getJSONObject(k)
                                    tracks.add(Track(trackItem.getInt("id"), trackItem.getString("name"), trackItem.getString("duration")))
                                }

                                val albumPerformersArray = albumItem.optJSONArray("performers") ?: JSONArray()
                                val albumPerformers = mutableListOf<Artist>()
                                for (k in 0 until albumPerformersArray.length()) {
                                    val performerItem = albumPerformersArray.getJSONObject(k)
                                    albumPerformers.add(Artist(
                                        id = performerItem.getInt("id"),
                                        name = performerItem.getString("name"),
                                        image = performerItem.optString("image"),
                                        description = performerItem.optString("description"),
                                        creationDate = performerItem.optString("creationDate", performerItem.optString("birthDate","")),
                                        albums = emptyList(),
                                        type = if (performerItem.has("birthDate")) Artist.ArtistType.MUSICIAN else Artist.ArtistType.BAND
                                    ))
                                }
                                val albumCommentsArray = albumItem.optJSONArray("comments") ?: JSONArray()
                                val albumComments = mutableListOf<Comment>()
                                for (k in 0 until albumCommentsArray.length()) {
                                    val commentItem = albumCommentsArray.getJSONObject(k)
                                    albumComments.add(Comment(commentItem.getInt("id"), commentItem.getString("description"), commentItem.getInt("rating")))
                                }


                                collectorAlbumsList.add(
                                    Album(
                                        id = albumItem.getInt("id"),
                                        name = albumItem.getString("name"),
                                        cover = albumItem.getString("cover"),
                                        description = albumItem.optString("description"),
                                        releaseDate = albumItem.optString("releaseDate"),
                                        genre = albumItem.optString("genre"),
                                        recordLabel = albumItem.optString("recordLabel"),
                                        tracks = tracks, // Lista de tracks parseada arriba
                                        performers = albumPerformers, // Lista de performers parseada arriba
                                        comments = albumComments // Lista de comments parseada arriba
                                    )
                                )
                            }

                            list.add(
                                Collector(
                                    id = item.getInt("id"),
                                    name = item.getString("name"),
                                    telephone = item.getString("telephone"),
                                    email = item.getString("email"),
                                    comments = commentsList,
                                    favoritePerformers = favoritePerformersList,
                                    collectorAlbums = collectorAlbumsList
                                    // avatarUrl fue removido del modelo Collector
                                )
                            )
                        }
                        onComplete(list)
                    } catch (e: Exception) {
                        onError(VolleyError("Error parsing collectors data: ${e.message}"))
                    }
                },
                { error ->
                    onError(error)
                })
        )
    }

    private fun getRequest(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }

}
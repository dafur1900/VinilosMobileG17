package com.example.vinilos.data.network

import android.content.Context
import android.util.Log
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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
                                        rating = commentItem.getInt("rating"),
                                        collector = commentItem.optInt("collector", 0)
                                    )
                                )
                            }

                            // Parsear favoritePerformers
                            val favoritePerformersArray = item.optJSONArray("favoritePerformers") ?: JSONArray()
                            val favoritePerformersList = mutableListOf<Artist>()
                            for (j in 0 until favoritePerformersArray.length()) {
                                val performerItem = favoritePerformersArray.getJSONObject(j)
                                val artistType = if (performerItem.has("birthDate")) Artist.ArtistType.MUSICIAN else Artist.ArtistType.BAND
                                val creationDate = if (artistType == Artist.ArtistType.MUSICIAN) 
                                    performerItem.optString("birthDate", "") 
                                else 
                                    performerItem.optString("creationDate", "")
                                    
                                favoritePerformersList.add(
                                    Artist(
                                        id = performerItem.getInt("id"),
                                        name = performerItem.getString("name"),
                                        image = performerItem.optString("image", ""),
                                        description = performerItem.optString("description", ""),
                                        creationDate = creationDate,
                                        albums = emptyList(),
                                        type = artistType
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
                                    favoritePerformers = favoritePerformersList
                                )
                            )
                        }
                        
                        // Log para depuración
                        println("DEBUG: Collectors parsed: ${list.size}")
                        
                        onComplete(list)
                    } catch (e: Exception) {
                        // Log para depuración
                        println("DEBUG: Error parsing collectors: ${e.message}")
                        e.printStackTrace()
                        onError(VolleyError("Error parsing collectors data: ${e.message}"))
                    }
                },
                { error ->
                    // Log para depuración
                    println("DEBUG: Network error: ${error.message}")
                    onError(error)
                })
        )
    }
    
    suspend fun getCollectorById(collectorId: Int): Collector = suspendCoroutine { cont ->
        requestQueue.add(
            StringRequest(Request.Method.GET, BASE_URL + "collectors/$collectorId",
                { response ->
                    try {
                        val item = JSONObject(response)
                        
                        // Parsear Comments
                        val commentsArray = item.optJSONArray("comments") ?: JSONArray()
                        val commentsList = mutableListOf<Comment>()
                        for (j in 0 until commentsArray.length()) {
                            val commentItem = commentsArray.getJSONObject(j)
                            commentsList.add(
                                Comment(
                                    id = commentItem.getInt("id"),
                                    description = commentItem.getString("description"),
                                    rating = commentItem.getInt("rating"),
                                    collector = commentItem.optInt("collector", 0)
                                )
                            )
                        }

                        // Parsear favoritePerformers
                        val favoritePerformersArray = item.optJSONArray("favoritePerformers") ?: JSONArray()
                        val favoritePerformersList = mutableListOf<Artist>()
                        for (j in 0 until favoritePerformersArray.length()) {
                            val performerItem = favoritePerformersArray.getJSONObject(j)
                            val artistType = if (performerItem.has("birthDate")) Artist.ArtistType.MUSICIAN else Artist.ArtistType.BAND
                            val creationDate = if (artistType == Artist.ArtistType.MUSICIAN) 
                                performerItem.optString("birthDate", "") 
                            else 
                                performerItem.optString("creationDate", "")
                                
                            favoritePerformersList.add(
                                Artist(
                                    id = performerItem.getInt("id"),
                                    name = performerItem.getString("name"),
                                    image = performerItem.optString("image", ""),
                                    description = performerItem.optString("description", ""),
                                    creationDate = creationDate,
                                    albums = emptyList(),
                                    type = artistType
                                )
                            )
                        }

                        val collector = Collector(
                            id = item.getInt("id"),
                            name = item.getString("name"),
                            telephone = item.getString("telephone"),
                            email = item.getString("email"),
                            comments = commentsList,
                            favoritePerformers = favoritePerformersList
                        )
                        
                        Log.d("NetworkServiceAdapter", "Collector details fetched: ${collector.name}")
                        
                        cont.resume(collector)
                    } catch (e: Exception) {
                        Log.e("NetworkServiceAdapter", "Error parsing collector details: ${e.message}", e)
                        cont.resumeWithException(e)
                    }
                },
                { error ->
                    Log.e("NetworkServiceAdapter", "Network error getting collector details: ${error.message}", error)
                    cont.resumeWithException(error)
                })
        )
    }

    suspend fun getCollectors(): List<Collector> = suspendCoroutine { cont ->
        requestQueue.add(
            StringRequest(Request.Method.GET, BASE_URL + "collectors",
                { response ->
                    try {
                        val respJsonArray = JSONArray(response)
                        val list = mutableListOf<Collector>()
                        
                        for (i in 0 until respJsonArray.length()) {
                            val item = respJsonArray.getJSONObject(i)
                            
                            // Parsear Comments
                            val commentsArray = item.optJSONArray("comments") ?: JSONArray()
                            val commentsList = mutableListOf<Comment>()
                            for (j in 0 until commentsArray.length()) {
                                val commentItem = commentsArray.getJSONObject(j)
                                commentsList.add(
                                    Comment(
                                        id = commentItem.getInt("id"),
                                        description = commentItem.getString("description"),
                                        rating = commentItem.getInt("rating"),
                                        collector = commentItem.optInt("collector", 0)
                                    )
                                )
                            }

                            // Parsear favoritePerformers
                            val favoritePerformersArray = item.optJSONArray("favoritePerformers") ?: JSONArray()
                            val favoritePerformersList = mutableListOf<Artist>()
                            for (j in 0 until favoritePerformersArray.length()) {
                                val performerItem = favoritePerformersArray.getJSONObject(j)
                                val artistType = if (performerItem.has("birthDate")) Artist.ArtistType.MUSICIAN else Artist.ArtistType.BAND
                                val creationDate = if (artistType == Artist.ArtistType.MUSICIAN) 
                                    performerItem.optString("birthDate", "") 
                                else 
                                    performerItem.optString("creationDate", "")
                                    
                                favoritePerformersList.add(
                                    Artist(
                                        id = performerItem.getInt("id"),
                                        name = performerItem.getString("name"),
                                        image = performerItem.optString("image", ""),
                                        description = performerItem.optString("description", ""),
                                        creationDate = creationDate,
                                        albums = emptyList(),
                                        type = artistType
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
                                    favoritePerformers = favoritePerformersList
                                )
                            )
                        }
                        
                        Log.d("NetworkServiceAdapter", "Collectors fetched: ${list.size}")
                        
                        cont.resume(list)
                    } catch (e: Exception) {
                        Log.e("NetworkServiceAdapter", "Error parsing collectors: ${e.message}", e)
                        cont.resumeWithException(e)
                    }
                },
                { error ->
                    Log.e("NetworkServiceAdapter", "Network error getting collectors: ${error.message}", error)
                    cont.resumeWithException(error)
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
    
    suspend fun getArtistById(artistId: Int, isMusician: Boolean): Artist = suspendCoroutine { cont ->
        val path = if (isMusician) "musicians/$artistId" else "bands/$artistId"
        requestQueue.add(
            StringRequest(Request.Method.GET, BASE_URL + path,
                { response ->
                    try {
                        val item = JSONObject(response)
                        val artist = if (isMusician) {
                            parseMusician(item)
                        } else {
                            parseBand(item)
                        }
                        Log.d("NetworkServiceAdapter", "Artist details fetched: ${artist.name}, Albums: ${artist.albums.size}")
                        cont.resume(artist)
                    } catch (e: Exception) {
                        Log.e("NetworkServiceAdapter", "Error parsing artist details: ${e.message}", e)
                        cont.resumeWithException(e)
                    }
                },
                { error ->
                    Log.e("NetworkServiceAdapter", "Network error getting artist details: ${error.message}", error)
                    cont.resumeWithException(error)
                })
        )
    }
    
    private fun parseMusician(item: JSONObject): Artist {
        val albumsArray = item.optJSONArray("albums") ?: JSONArray()
        val albums = mutableListOf<Album>()
        for (j in 0 until albumsArray.length()) {
            val albumItem = albumsArray.getJSONObject(j)
            albums.add(
                Album(
                    id = albumItem.getInt("id"),
                    name = albumItem.getString("name"),
                    cover = albumItem.getString("cover"),
                    description = albumItem.getString("description"),
                    releaseDate = albumItem.getString("releaseDate"),
                    genre = albumItem.getString("genre"),
                    recordLabel = albumItem.getString("recordLabel"),
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                )
            )
        }

        return Artist(
            id = item.getInt("id"),
            name = item.getString("name"),
            image = item.getString("image"),
            description = item.getString("description"),
            creationDate = item.optString("birthDate", ""),
            albums = albums.sortedBy { it.name },
            type = Artist.ArtistType.MUSICIAN
        )
    }

    private fun parseBand(item: JSONObject): Artist {
        val albumsArray = item.optJSONArray("albums") ?: JSONArray()
        val albums = mutableListOf<Album>()
        for (j in 0 until albumsArray.length()) {
            val albumItem = albumsArray.getJSONObject(j)
            albums.add(
                Album(
                    id = albumItem.getInt("id"),
                    name = albumItem.getString("name"),
                    cover = albumItem.getString("cover"),
                    description = albumItem.getString("description"),
                    releaseDate = albumItem.getString("releaseDate"),
                    genre = albumItem.getString("genre"),
                    recordLabel = albumItem.getString("recordLabel"),
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                )
            )
        }

        return Artist(
            id = item.getInt("id"),
            name = item.getString("name"),
            image = item.getString("image"),
            description = item.getString("description"),
            creationDate = item.optString("creationDate", ""),
            albums = albums.sortedBy { it.name },
            type = Artist.ArtistType.BAND
        )
    }

}
package org.gabrieal.gymtracker.data.network

object APIService {
    internal const val spotifyClientId = "[SPOTIFY_CLIENT_ID]"
    internal const val spotifyClientSecret = "[SPOTIFY_CLIENT_SECRET]"
    fun spotifyTrackPath(trackId: String): String = "https://api.spotify.com/v1/tracks?ids=$trackId"
    fun spotifyRequestTokenUrl(): String = "https://accounts.spotify.com/api/token"
}

fun getSpotifyBody() =
    buildString {
        append("grant_type=client_credentials")
        append("&client_id=${APIService.spotifyClientId}")
        append("&client_secret=${APIService.spotifyClientSecret}")
    }
package org.gabrieal.gymtracker.data.network

import io.ktor.http.encodeURLParameter
import org.gabrieal.gymtracker.util.systemUtil.SPOTIFY_CLIENT_ID
import org.gabrieal.gymtracker.util.systemUtil.SPOTIFY_CLIENT_SECRET

object APIService {
    internal const val spotifyClientId = SPOTIFY_CLIENT_ID
    internal const val spotifyClientSecret = SPOTIFY_CLIENT_SECRET

    internal const val spotifyRedirectUri = "gabriealgymtracker://callback"
    fun spotifyTrackPath(trackId: String): String = "https://api.spotify.com/v1/tracks?ids=$trackId"
    fun spotifyPlaybackPath(): String = "https://api.spotify.com/v1/me/player"
    fun spotifyRequestTokenUrl(): String = "https://accounts.spotify.com/api/token"

    fun authUrl(codeChallenge: String): String =
        buildString {
            append("https://accounts.spotify.com/authorize?")
            append("client_id=$spotifyClientId")
            append("&response_type=code")
            append("&redirect_uri=${spotifyRedirectUri.encodeURLParameter()}")
            append("&code_challenge_method=S256")
            append("&code_challenge=$codeChallenge")
            append("&scope=")
            append(listOf(
                "user-read-playback-state",
                "user-modify-playback-state",
                "user-read-currently-playing"
            ).joinToString("%20"))
        }
}

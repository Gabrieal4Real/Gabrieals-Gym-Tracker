package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable


enum class SpotifyPlayerState(val playerState: String, val seekable: Boolean) {
    PLAY("play", false),
    PAUSE("pause", false),
    NEXT("next", true),
    PREVIOUS("previous", true),
}

@Serializable
data class SpotifyTracks(
    val tracks: List<SpotifyTrack>? = null,
)

@Serializable
data class SpotifyTrack(
    val name: String? = null,
    val artists: List<Artist>? = null,
    val album: Album? = null,
    val duration_ms: Long? = null
)

@Serializable
data class Artist(
    val name: String? = null
)

@Serializable
data class Album(
    val name: String? = null,
    val images: List<AlbumImage>? = null
)

@Serializable
data class AlbumImage(
    val url: String? = null,
    val height: Int? = null,
    val width: Int? = null
)

@Serializable
data class SpotifyProfile(
    val display_name: String? = null,
    val images: List<AlbumImage>? = null,
    val product: String? = null,
    val email: String? = null
)

@Serializable
data class SpotifyPlayback(
    val device: Device? = null,
    val is_playing: Boolean? = null,
    val item: SpotifyTrack? = null,
    val progress_ms: Long? = null
)

@Serializable
data class Device(val id: String? = null)
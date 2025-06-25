package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyTracks(
    val track: List<SpotifyTrack>,
)

@Serializable
data class SpotifyTrack(
    val name: String,
    val artists: List<Artist>,
    val album: Album
)

@Serializable
data class Artist(val name: String)

@Serializable
data class Album(
    val name: String,
    val images: List<AlbumImage>
)

@Serializable
data class AlbumImage(
    val url: String,
    val height: Int,
    val width: Int
)
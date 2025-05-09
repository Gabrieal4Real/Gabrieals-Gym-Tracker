package org.gabrieal.gymtracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
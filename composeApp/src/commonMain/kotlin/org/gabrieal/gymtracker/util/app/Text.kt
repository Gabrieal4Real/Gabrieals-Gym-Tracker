package org.gabrieal.gymtracker.util.app

fun String?.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return emailRegex.matches(this ?: "")
}

fun String?.isValidPassword(): Boolean {
    // At least 8 characters, with at least one uppercase, one lowercase, one digit, and one special character
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()
    return passwordRegex.matches(this ?: "")
}
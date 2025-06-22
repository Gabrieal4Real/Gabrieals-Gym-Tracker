package org.gabrieal.gymtracker.util.app

import kotlin.math.roundToInt

val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()

// At least 8 characters, with at least one uppercase, one lowercase, one digit, and one special character
val passwordRegex =
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()
val nameRegex = "^[a-zA-Z ]+$".toRegex()
val numberRegex = "^\\d*$".toRegex()
val decimalRegex = "^\\d*(\\.\\d*)?$".toRegex()

fun String?.isValidEmail() = emailRegex.matches(this ?: "")

fun String?.isValidPassword() = passwordRegex.matches(this ?: "")

fun String?.isValidName() = nameRegex.matches(this ?: "")

fun String?.isValidNumber() = numberRegex.matches(this ?: "")

fun String?.isValidDecimal() = decimalRegex.matches(this ?: "")

fun Double.roundTwoDecimal() = (this * 100.0).roundToInt() / 100.0
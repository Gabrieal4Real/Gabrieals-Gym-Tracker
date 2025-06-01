package org.gabrieal.gymtracker.util.app

import androidx.compose.ui.graphics.Color

abstract class AppColors {
    abstract val background: Color
    abstract val lighterBackground: Color
    abstract val cardBackground: Color
    abstract val borderStroke: Color
    abstract val textPrimary: Color
    abstract val textSecondary: Color
    abstract val linkBlue: Color
    abstract val slightlyDarkerLinkBlue: Color
    abstract val placeholderColor: Color
    abstract val white: Color
    abstract val black: Color
    abstract val maroon: Color
    abstract val lightMaroon: Color
    abstract val deleteRed: Color
    abstract val checkMarkGreen: Color
    abstract val bottomNavIndicator: Color
}

object DarkColors : AppColors() {
    override val background = Color(0xFF232323)
    override val lighterBackground = Color(0xFF2E2E2E)
    override val cardBackground = Color(0xFF393E46)
    override val borderStroke = Color(0xFF948979)
    override val textPrimary = Color(0xFFE3E3E3)
    override val textSecondary = Color(0xFFDFD0B8)
    override val linkBlue = Color(0xFF1E88E5)
    override val slightlyDarkerLinkBlue = Color(0xFF156DB6)
    override val placeholderColor = Color(0x996C757D)

    override val white = Color(0xFFFFFFFF)
    override val black = Color(0xFF000000)
    override val maroon = Color(0xFF670D2F)
    override val lightMaroon = Color(0xFFAB4567)
    override val deleteRed = Color(0xFFB22222)
    override val checkMarkGreen = Color(0xFFA0C878)
    override val bottomNavIndicator = Color(0xFF096B68)
}

object LightColors : AppColors() {
    override val background = Color(0xFF889E81)
    override val lighterBackground = Color(0xFFC8DBBE)
    override val cardBackground = Color(0xFFBAC7A7)
    override val borderStroke = Color(0xFF676055)
    override val textPrimary = Color(0xFF232323)
    override val textSecondary = Color(0xFF3F362B)
    override val linkBlue = Color(0xFF1976D2)
    override val slightlyDarkerLinkBlue = Color(0xFF1565C0)
    override val placeholderColor = Color(0xCC5A646B)

    override val white = Color(0xFFFFFFFF)
    override val black = Color(0xFF000000)
    override val maroon = Color(0xFFA53860)
    override val lightMaroon = Color(0xFFAB4567)
    override val deleteRed = Color(0xFFD32F2F)
    override val checkMarkGreen = Color(0xFFA0C878)
    override val bottomNavIndicator = Color(0xFF096B68)
}
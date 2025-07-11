package org.gabrieal.gymtracker.util.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import gymtracker.composeapp.generated.resources.Poppins_Bold
import gymtracker.composeapp.generated.resources.Poppins_ExtraBold
import gymtracker.composeapp.generated.resources.Poppins_Medium
import gymtracker.composeapp.generated.resources.Poppins_Regular
import gymtracker.composeapp.generated.resources.Poppins_SemiBold
import gymtracker.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun ExtraBoldText() = FontFamily(Font(Res.font.Poppins_ExtraBold))

@Composable
fun BoldText() = FontFamily(Font(Res.font.Poppins_Bold))

@Composable
fun MediumText() = FontFamily(Font(Res.font.Poppins_Medium))

@Composable
fun RegularText() = FontFamily(Font(Res.font.Poppins_Regular))

@Composable
fun SemiBoldText() = FontFamily(Font(Res.font.Poppins_SemiBold))
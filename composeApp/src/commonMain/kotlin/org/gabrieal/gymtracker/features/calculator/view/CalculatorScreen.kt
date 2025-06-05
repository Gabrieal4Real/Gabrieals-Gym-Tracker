package org.gabrieal.gymtracker.features.calculator.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.icon_protein
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.features.calculator.viewmodel.CalculatorViewModel
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.ProteinInput
import org.gabrieal.gymtracker.util.app.calculateProteinGrams
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.FitnessGoal
import org.gabrieal.gymtracker.util.widgets.AnimatedDividerWithScale
import org.gabrieal.gymtracker.util.widgets.BackButtonRow
import org.gabrieal.gymtracker.util.widgets.BiggerText
import org.gabrieal.gymtracker.util.widgets.CustomTextField
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.DropdownMenuBox
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.gabrieal.gymtracker.util.widgets.popOut
import org.jetbrains.compose.resources.painterResource

object CalculatorScreen : Screen {
    private val viewModel = CalculatorViewModel()

    lateinit var title: String

    fun setProfile(profile: Profile) {
        viewModel.setProfile(profile)
        viewModel.setWeight(profile.weight?.toInt())
        viewModel.setGoal(profile.goal)
        viewModel.setActivityLevel(profile.activityLevel)
    }

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()
        val weight = uiState.weight
        val goal = uiState.goal
        val activityLevel = uiState.activityLevel

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow("Calculator")

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.lighterBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                    // Header
                    TinyText(
                        "Let's start calculating your",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    BiggerText(
                        title,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .scale(popOut().value)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedDividerWithScale()
                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn {
                        item {
                            TinyItalicText("What is your weight (kg)?")
                            Spacer(modifier = Modifier.height(8.dp))

                            CustomTextField(
                                value = weight?.let { "$it" } ?: "",
                                onValueChange = { weight ->
                                    val numberRegex = Regex("^\\d*\$")
                                    if (weight.isEmpty() || weight.matches(numberRegex)) {
                                        viewModel.setWeight(weight.toIntOrNull())
                                    }
                                },
                                placeholderText = "75 KG",
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            TinyItalicText("What is your fitness goal?")
                            Spacer(modifier = Modifier.height(8.dp))
                            DropdownMenuBox(
                                value = goal?.displayName ?: "",
                                placeholderText = FitnessGoal.MAINTENANCE.displayName,
                                options = FitnessGoal.entries.map { it.displayName },
                                onSelected = { fitnessGoal ->
                                    viewModel.setGoal(
                                        FitnessGoal.entries.find { it.displayName == fitnessGoal }
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            TinyItalicText("What is your activity level?")
                            Spacer(modifier = Modifier.height(8.dp))
                            DropdownMenuBox(
                                value = activityLevel?.displayName ?: "",
                                placeholderText = ActivityLevel.MODERATELY_ACTIVE.displayName,
                                options = ActivityLevel.entries.map { it.description },
                                onSelected = { activityLevel ->
                                    viewModel.setActivityLevel(
                                        ActivityLevel.entries.find { it.description == activityLevel }
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            if (weight != null && goal != null && activityLevel != null) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.icon_protein),
                                        contentDescription = "Protein",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.height(120.dp),
                                        colorFilter = ColorFilter.tint(colors.textPrimary)
                                    )
                                    Column {
                                        DescriptionText("Your recommended protein intake is:")
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val proteinInput = ProteinInput(weight, goal, activityLevel)
                                        SubtitleText("${calculateProteinGrams(proteinInput)} grams/day")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
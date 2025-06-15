package org.gabrieal.gymtracker.features.calculator.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.ProteinInput
import org.gabrieal.gymtracker.features.calculator.viewmodel.CalculatorViewModel
import org.gabrieal.gymtracker.util.app.calculateProteinGrams
import org.gabrieal.gymtracker.util.app.isValidNumber
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CalculatorScreen : Screen, KoinComponent {
    private val viewModel: CalculatorViewModel by inject()

    lateinit var title: String

    fun setProfile(profile: Profile) {
        with(viewModel) {
            setProfile(profile)
            setWeight(profile.weight?.toInt())
            setGoal(profile.goal)
            setActivityLevel(profile.activityLevel)
        }
    }

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()
        val (weight, goal, activityLevel) = Triple(uiState.weight, uiState.goal, uiState.activityLevel)

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
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    HeaderSection()

                    InputSection(weight, goal, activityLevel)

                    AnimatedVisibility(
                        visible = weight != null && goal != null && activityLevel != null,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        if (weight != null && goal != null && activityLevel != null) {
                            ResultSection(weight, goal, activityLevel)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HeaderSection() {
        TinyText("Let's start calculating your")
        BiggerText(
            title,
            modifier = Modifier
                .scale(popOut().value)
        )
        Spacer(Modifier.height(8.dp))
        AnimatedDividerWithScale()
        Spacer(Modifier.height(24.dp))
    }

    @Composable
    private fun InputSection(weight: Int?, goal: FitnessGoal?, activityLevel: ActivityLevel?) {
        InputField(
            label = "What is your weight (kg)?",
            value = weight?.toString() ?: "",
            placeholder = "75 KG",
            onChange = { input ->
                if (input.isEmpty() || input.isValidNumber())
                    viewModel.setWeight(input.toIntOrNull())
            }
        )

        DropdownField(
            label = "What is your fitness goal?",
            value = goal?.displayName,
            placeholder = FitnessGoal.MAINTENANCE.displayName,
            options = FitnessGoal.entries.map { it.displayName },
            onSelect = { selected ->
                viewModel.setGoal(FitnessGoal.entries.find { it.displayName == selected })
            }
        )

        DropdownField(
            label = "What is your activity level?",
            value = activityLevel?.displayName,
            placeholder = ActivityLevel.MODERATELY_ACTIVE.displayName,
            options = ActivityLevel.entries.map { it.description },
            onSelect = { selected ->
                viewModel.setActivityLevel(ActivityLevel.entries.find { it.description == selected })
            }
        )
    }

    @Composable
    private fun ResultSection(weight: Int, goal: FitnessGoal, activityLevel: ActivityLevel) {
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
                SubtitleText("${calculateProteinGrams(ProteinInput(weight, goal, activityLevel))} grams/day")
            }
        }
    }

    @Composable
    private fun InputField(label: String, value: String, placeholder: String, onChange: (String) -> Unit) {
        TinyItalicText(label)
        Spacer(modifier = Modifier.height(8.dp))
        CustomTextField(
            value = value,
            onValueChange = onChange,
            placeholderText = placeholder
        )
        Spacer(modifier = Modifier.height(24.dp))
    }

    @Composable
    private fun DropdownField(
        label: String,
        value: String?,
        placeholder: String,
        options: List<String>,
        onSelect: (String) -> Unit
    ) {
        TinyItalicText(label)
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenuBox(
            value = value ?: "",
            placeholderText = placeholder,
            options = options,
            onSelected = onSelect
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
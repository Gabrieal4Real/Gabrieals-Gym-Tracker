package org.gabrieal.gymtracker.views.screens.landingTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Height
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.appUtil.getBMISummary
import org.gabrieal.gymtracker.util.appUtil.getListForWeightHeightAgeSpinner
import org.gabrieal.gymtracker.util.systemUtil.ShowSpinner
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.viewmodel.profile.ProfileViewModel
import org.gabrieal.gymtracker.views.colors
import org.gabrieal.gymtracker.views.widgets.CustomCard
import org.gabrieal.gymtracker.views.widgets.DashedDivider
import org.gabrieal.gymtracker.views.widgets.DescriptionText
import org.gabrieal.gymtracker.views.widgets.IconNext
import org.gabrieal.gymtracker.views.widgets.SubtitleText
import org.gabrieal.gymtracker.views.widgets.TinyText
import org.gabrieal.gymtracker.views.widgets.TitleRow

object ProfileTab : Tab {
    private val viewModel = ProfileViewModel()

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val routines = uiState.selectedRoutineList
        val profile = uiState.profile
        val weight = profile?.weight
        val height = profile?.height
        val age = profile?.age

        val saveRoutineList = uiState.saveRoutineList
        val saveProfile = uiState.saveProfile
        val weightHeightBMIClicked = uiState.weightHeightBMIClicked

        val context = getCurrentContext()

        BackHandler(enabled = true) {}

        LaunchedEffect(context) {
            viewModel.updateContext(context)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleRow("Profile")
            Box(
                modifier = Modifier.fillMaxSize().background(colors.lighterBackground)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    item {
                        WeightHeightAgeCard(weight, height, age)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        EditRoutinesCard(routines)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        CalculatorRow(
                            listOf(
                                Triple(Icons.Rounded.WaterDrop, "Protein Intake Calculator") {
                                    viewModel.navigateToProteinCalculator()
                                },
                                Triple(Icons.Rounded.PieChart, "Maintenance Calorie Calculator") {
                                    viewModel.navigateToMaintenanceCalculator()
                                }
                            )
                        )
                    }
                }
            }
        }

        if (saveProfile) {
            viewModel.saveProfile()
        }

        if (saveRoutineList) {
            viewModel.saveRoutineList()
        }

        if (weightHeightBMIClicked != -1) {
            val list = getListForWeightHeightAgeSpinner(weightHeightBMIClicked)
            ShowSpinner(
                title = "Select your ${if (weightHeightBMIClicked == 0) "weight" else if (weightHeightBMIClicked == 2) "age" else "height"}",
                options = list,
                onOptionSelected = { index ->
                    if (index == -1) {
                        viewModel.setWeightHeightBMIClicked(-1)
                        return@ShowSpinner
                    }
                    when (weightHeightBMIClicked) {
                        0 -> profile?.weight = list[index].removeSuffix(" KG").toDouble()
                        1 -> profile?.height = list[index].removeSuffix(" CM").toDouble()
                        2 -> profile?.age = list[index].toInt()
                    }
                    viewModel.setWeightHeightBMIClicked(-1)
                    profile?.let { viewModel.updateProfile(it) }
                }
            )
        }
    }


    @Composable
    fun CalculatorRow(cards: List<Triple<ImageVector, String, () -> Unit>>) {
        Row(modifier = Modifier.fillMaxWidth()) {
            val itemModifier = Modifier.weight(1f)

            cards.forEachIndexed { index, (icon, title, onClick) ->
                CalculatorCard(
                    modifier = itemModifier,
                    calculatorCard = Triple(icon, title, onClick)
                )

                if (index != cards.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }


    @Composable
    fun CalculatorCard(
        modifier: Modifier = Modifier,
        calculatorCard: Triple<ImageVector, String, () -> Unit>
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            CustomCard(
                enabled = true,
                onClick = {
                    calculatorCard.third.invoke()
                },
                content = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = calculatorCard.first,
                            contentDescription = calculatorCard.second,
                            tint = colors.textPrimary,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TinyText(calculatorCard.second, textAlign = TextAlign.Center)
                    }
                }
            )
        }
    }


    @Composable
    fun EditRoutinesCard(routines: List<SelectedExerciseList>) {
        CustomCard(
            enabled = true,
            content = {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SubtitleText("Edit Current Routine")
                    IconNext()
                }
            },
            onClick = {
                viewModel.navigateToEditSplit(routines)
            }
        )
    }

    @Composable
    fun WeightHeightAgeCard(weight: Double?, height: Double?, age: Int?) {
        CustomCard(
            enabled = true,
            content = {
                val listOfTypes = listOf("Weight", "Height", "Age")
                val typeOfData = listOf(
                    Pair(Triple(weight?.toInt(), Icons.Rounded.MonitorWeight, " KG")) {
                        viewModel.setWeightHeightBMIClicked(0)
                    },
                    Pair(Triple(height?.toInt(), Icons.Rounded.Height, " CM")) {
                        viewModel.setWeightHeightBMIClicked(1)
                    },
                    Pair(Triple(age, Icons.Rounded.PersonOutline, "")) {
                        viewModel.setWeightHeightBMIClicked(2)
                    },
                )
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        typeOfData.forEachIndexed { index, pair ->
                            val title: String =
                                if (pair.first.first != null) "${pair.first.first}${pair.first.third}" else "No Data"
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f).clickable {
                                    pair.second.invoke()
                                }) {
                                DescriptionText(listOfTypes[index])
                                Spacer(modifier = Modifier.height(4.dp))
                                Icon(
                                    painter = rememberVectorPainter(pair.first.second),
                                    tint = colors.textPrimary,
                                    contentDescription = pair.first.third
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                TinyText(title)
                            }
                            if (index != typeOfData.lastIndex) {
                                Spacer(modifier = Modifier.width(8.dp))
                                DashedDivider()
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }

                    getBMISummary(weight, height)
                }
            }
        )
    }

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 2u,
            title = "Profile",
            icon = rememberVectorPainter(Icons.Rounded.Person),
        )
}

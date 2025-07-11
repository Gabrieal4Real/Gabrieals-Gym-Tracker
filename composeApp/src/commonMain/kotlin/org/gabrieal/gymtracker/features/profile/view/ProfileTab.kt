package org.gabrieal.gymtracker.features.profile.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Height
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.SportsGymnastics
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.FirebaseInfo
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.features.profile.viewmodel.ProfileViewModel
import org.gabrieal.gymtracker.util.app.getBMISummary
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.util.systemUtil.ShowInputDialog
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.DashedDivider
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.LinkText
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.gabrieal.gymtracker.util.widgets.TitleRow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ProfileTab : Tab, KoinComponent {
    private val viewModel: ProfileViewModel by inject()

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val routines = uiState.selectedRoutineList
        val profile = uiState.profile
        val weightHeightBMIClicked = uiState.weightHeightBMIClicked
        val firebaseInfo = uiState.firebaseInfo
        val loggingOut = uiState.loggingOut

        BackHandler(enabled = true) {}

        LaunchedEffect(Unit) {
            viewModel.updateContext()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleRow("Profile")
            Box(
                modifier = Modifier.fillMaxSize().background(colors.lighterBackground)
                    .padding(horizontal = 16.dp),
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier.fillMaxSize().animateContentSize()
                ) {
                    item {
                        ProfileCard(profile, firebaseInfo)
                    }

                    item {
                        CalculatorRow(
                            listOf(
                                Triple(Icons.Rounded.SportsGymnastics, "Edit Workout") {
                                    viewModel.navigateToEditSplit(routines)
                                },
                                Triple(Icons.Rounded.History, "Workout History") {
                                    viewModel.navigateToWorkoutHistory()
                                },
                            )
                        )
                    }

                    if (profile?.weight != null && profile.height != null)
                        item {
                            Column(modifier = Modifier.fillMaxWidth().animateItem()) {
                                getBMISummary(profile.weight, profile.height)
                            }
                        }

                    item {
                        CalculatorRow(
                            listOf(
                                Triple(Icons.Rounded.WaterDrop, "Protein Intake Calculator") {
                                    viewModel.navigateToProteinCalculator()
                                },
                                Triple(Icons.Rounded.PieChart, "Maintenance Calorie Calculator") {
                                    viewModel.navigateToMaintenanceCalculator()
                                },
                            )
                        )
                    }
                }
            }
        }

        if (loggingOut) {
            ShowAlertDialog(
                Pair("Logout", "Are you sure you want to logout?"),
                positiveButton = "Logout" to {
                    viewModel.logout()
                },
                negativeButton = "Cancel" to {
                    viewModel.setLoggingOut(false)
                }
            )
        }

        if (weightHeightBMIClicked != -1) {
            val title =
                if (weightHeightBMIClicked == 0) "Weight" else if (weightHeightBMIClicked == 2) "Age" else "Height"
            val message =
                "Enter your ${if (weightHeightBMIClicked == 0) "weight (kg)" else if (weightHeightBMIClicked == 1) "height (cm)" else "age"}"
            ShowInputDialog(
                titleMessage = Pair(title, message),
                positiveButton = "Save" to {
                    when (weightHeightBMIClicked) {
                        0 -> viewModel.updateWeight(it.toDouble())
                        1 -> viewModel.updateHeight(it.toDouble())
                        2 -> viewModel.updateAge(it.toInt())
                    }
                    viewModel.setWeightHeightBMIClicked(-1)
                },
                negativeButton = "Cancel" to { viewModel.setWeightHeightBMIClicked(-1) },
                type = KeyboardType.Number
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
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = calculatorCard.first,
                            contentDescription = calculatorCard.second,
                            tint = colors.textPrimary,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TinyText(calculatorCard.second, textAlign = TextAlign.Center)
                    }
                }
            )
        }
    }

    @Composable
    fun ProfileCard(profile: Profile?, firebaseInfo: FirebaseInfo?) {
        val weight = profile?.weight
        val height = profile?.height
        val age = profile?.age

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
                        modifier = Modifier.fillMaxWidth().clickable {
                            if (firebaseInfo?.uid == null || firebaseInfo.token == null) {
                                viewModel.navigateToLoginRegister()
                            }
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "Profile",
                            tint = colors.textPrimary,
                            modifier = Modifier.size(60.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            val name = profile?.userName ?: "Not Logged In"
                            val gender = profile?.gender?.name ?: "Gender Unspecified"

                            SubtitleText(name.uppercase())
                            TinyText(gender)

                            if (firebaseInfo?.uid == null || firebaseInfo.token == null) {
                                LinkText("You're not logged in")
                            }
                        }
                        if (firebaseInfo?.uid != null && firebaseInfo.token != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Logout,
                                contentDescription = "Logout",
                                tint = colors.white,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colors.deleteRed)
                                    .clickable {
                                        viewModel.setLoggingOut(true)
                                    }
                                    .padding(6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        typeOfData.forEachIndexed { index, pair ->
                            val title =
                                pair.first.first?.let { "$it${pair.first.third}" } ?: "No Data"

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
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 2u,
            title = "Profile",
            icon = rememberVectorPainter(Icons.Rounded.Person),
        )
}

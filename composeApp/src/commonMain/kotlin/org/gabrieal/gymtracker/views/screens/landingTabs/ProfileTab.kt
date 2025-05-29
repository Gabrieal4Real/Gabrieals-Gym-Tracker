package org.gabrieal.gymtracker.views.screens.landingTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.util.systemUtil.notifyPlatform
import org.gabrieal.gymtracker.util.systemUtil.requestPermission
import org.gabrieal.gymtracker.viewmodel.profile.ProfileViewModel
import org.gabrieal.gymtracker.views.colors
import org.gabrieal.gymtracker.views.widgets.ConfirmButton
import org.gabrieal.gymtracker.views.widgets.TitleRow

object ProfileTab : Tab {
    private val viewModel = ProfileViewModel()

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()
        val routines = uiState.selectedRoutineList

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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ConfirmButton("Request Permission", onClick = {
                        requestPermission()
                    })
                    ConfirmButton("Test Notification", onClick = {
                        notifyPlatform("Notification")
                    })
                }
                //Profile

                //Edit Routines

                //Macronutrient Split Calculator

                //Protein Intake Calculator

                //TDEE (Total Daily Energy Expenditure)

                //BMI Calculator

                //Maintenance Calorie Calculator
                    //Calorie Tracker
            }
        }
    }

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 2u,
            title = "Profile",
            icon = rememberVectorPainter(Icons.Rounded.Person),
        )
}

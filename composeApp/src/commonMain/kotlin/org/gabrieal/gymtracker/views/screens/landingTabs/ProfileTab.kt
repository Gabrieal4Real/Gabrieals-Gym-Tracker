package org.gabrieal.gymtracker.views.screens.landingTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.gabrieal.gymtracker.viewmodel.home.HomeViewModel
import org.gabrieal.gymtracker.views.colors
import org.gabrieal.gymtracker.views.widgets.TitleRow

object ProfileTab : Tab {
    private val viewModel = HomeViewModel()

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleRow("Profile")
            Box(
                modifier = Modifier.fillMaxSize().background(colors.lighterBackground)
            ) {

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

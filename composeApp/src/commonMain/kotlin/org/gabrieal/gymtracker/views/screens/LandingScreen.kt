package org.gabrieal.gymtracker.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.gabrieal.gymtracker.views.colors
import org.gabrieal.gymtracker.views.widgets.TinyText

object LandingScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(HomeScreen) {
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = colors.background,
                    ) {
                        TabNavigationItem(ViewAllWorkoutScreen)
                        TabNavigationItem(HomeScreen)
                    }
                }
            ) {
                Column(modifier = Modifier.padding(it).background(colors.background)) {
                    CurrentTab()
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = colors.textPrimary,
            selectedTextColor = colors.textPrimary,
            unselectedIconColor = colors.textPrimary,
            unselectedTextColor = colors.textPrimary,
            indicatorColor = colors.bottomNavIndicator
        ),
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = { TinyText(tab.options.title) },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        }
    )
}
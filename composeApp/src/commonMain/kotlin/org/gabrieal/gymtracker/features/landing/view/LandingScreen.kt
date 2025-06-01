package org.gabrieal.gymtracker.features.landing.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
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
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.features.home.view.HomeTab
import org.gabrieal.gymtracker.features.profile.view.ProfileTab
import org.gabrieal.gymtracker.features.viewAllWorkouts.view.ViewAllWorkoutTabScreen
import org.gabrieal.gymtracker.util.widgets.TinyText

object LandingScreen : Screen {
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun Content() {
        TabNavigator(HomeTab) { tabNavigator ->
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = colors.background,
                    ) {
                        TabNavigationItem(ViewAllWorkoutTabScreen)
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(ProfileTab)
                    }
                }
            ) {

                Column(modifier = Modifier.padding(it).background(colors.background)) {
                    Crossfade(targetState = tabNavigator.current) { tab ->
                        tab.Content()
                    }
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
package org.gabrieal.gymtracker.features.loginRegister.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.features.editPlan.view.EditPlanScreen
import org.gabrieal.gymtracker.features.loginRegister.viewmodel.LoginRegisterViewModel
import org.gabrieal.gymtracker.util.widgets.CustomTextField
import org.gabrieal.gymtracker.util.widgets.TitleRow
import org.gabrieal.gymtracker.util.widgets.TitleText
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object LoginRegisterBottomSheet : Screen, KoinComponent {
    val viewModel: LoginRegisterViewModel by inject()
    
    fun setProfile(profile: Profile) {
        viewModel.setProfile(profile)
    }

    fun setCallback(callback: (Profile?) -> Unit) {
        viewModel.setCallback(callback)
    }

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val profile = uiState.profile
        val isRegisterMode = uiState.isRegisterMode
        val password = uiState.password

        Box(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleRow(if (isRegisterMode) "Register" else "Login")

                if (isRegisterMode) {
                    CustomTextField(
                        value = profile.userName ?: "",
                        onValueChange = { userName ->
                            viewModel.updateProfileName(userName)
                        },
                        placeholderText = "John Wick",
                        resource = Icons.Rounded.Person to {}
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                CustomTextField(
                    value = profile.email ?: "",
                    onValueChange = { email ->
                        viewModel.updateProfileEmail(email)
                    },
                    placeholderText = "jonathan.wick@continental.com",
                    resource = Icons.Rounded.Email to {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = password ?: "",
                    onValueChange = { password ->
                        viewModel.updatePassword(password)
                    },
                    placeholderText = "th1nk1ngImBacK",
                    resource = Icons.Rounded.Password to {}
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
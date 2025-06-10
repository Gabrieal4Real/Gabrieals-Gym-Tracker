package org.gabrieal.gymtracker.features.loginRegister.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.gabrieal.gymtracker.features.loginRegister.viewmodel.LoginRegisterViewModel
import org.gabrieal.gymtracker.util.app.isValidEmail
import org.gabrieal.gymtracker.util.app.isValidPassword
import org.gabrieal.gymtracker.util.systemUtil.ShowToast
import org.gabrieal.gymtracker.util.widgets.ConfirmButton
import org.gabrieal.gymtracker.util.widgets.CustomTextField
import org.gabrieal.gymtracker.util.widgets.LinkText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TitleRow
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

        val isRegisterMode = uiState.isRegisterMode
        val userName = uiState.userName
        val email = uiState.email
        val password = uiState.password
        val error = uiState.error

        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(colors.background)
        ) {
            if (!error.isNullOrBlank()) {
                ShowToast(error)
                viewModel.updateError()
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleRow(if (isRegisterMode) "Register" else "Login")

                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                ) {
                    if (isRegisterMode) {
                        TinyItalicText("Username")
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(
                            value = userName ?: "",
                            onValueChange = { userName ->
                                viewModel.updateName(userName)
                            },
                            placeholderText = "John Wick",
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    TinyItalicText("Email")
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = email ?: "",
                        onValueChange = { email ->
                            viewModel.updateEmail(email)
                        },
                        placeholderText = "jonathan.wick@continental.com",
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TinyItalicText("Password")
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = password ?: "",
                        onValueChange = { password ->
                            viewModel.updatePassword(password)
                        },
                        placeholderText = "th1nk1ngImBacK",
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    TinyItalicText("You don't need an account to use this app, but you can create one if you want.")
                    Spacer(modifier = Modifier.height(4.dp))
                    ConfirmButton(
                        if (isRegisterMode) "Register" else "Login",
                        onClick = {
                            if (isRegisterMode) {
                                viewModel.registerNewUser(
                                    email = email ?: "",
                                    password = password ?: ""
                                )
                            } else {
                                viewModel.loginExistingUser(
                                    email = email ?: "",
                                    password = password ?: ""
                                )
                            }
                        },
                        enabled = isEnabled(isRegisterMode, userName, email, password),
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinkText(
                        if (isRegisterMode) "Already have an account? Login now" else "Don't have an account? Register now",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .clickable { viewModel.changeRegisterMode() })

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    private fun isEnabled(
        isRegisterMode: Boolean,
        userName: String?,
        email: String?,
        password: String?
    ): Boolean {
        val isEmailValid = !email.isNullOrBlank() && email.isValidEmail()
        val isPasswordValid = !password.isNullOrBlank() && password.isValidPassword()

        return if (isRegisterMode) {
            !userName.isNullOrBlank() && isEmailValid && isPasswordValid
        } else {
            isEmailValid && isPasswordValid
        }
    }
}
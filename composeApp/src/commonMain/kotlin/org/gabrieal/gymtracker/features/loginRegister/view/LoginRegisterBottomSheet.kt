package org.gabrieal.gymtracker.features.loginRegister.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepoImpl
import org.gabrieal.gymtracker.features.loginRegister.viewmodel.LoginRegisterViewModel
import org.gabrieal.gymtracker.network.FirebaseService
import org.gabrieal.gymtracker.util.systemUtil.httpClient

object LoginRegisterBottomSheet : Screen {
    val firebaseService = FirebaseService(httpClient)
    val loginRegisterRepoImpl = LoginRegisterRepoImpl(firebaseService)
    val loginRegisterViewModel = LoginRegisterViewModel(loginRegisterRepoImpl)

    @Composable
    override fun Content() {

        LaunchedEffect(Unit) {
            loginRegisterViewModel.registerNewUser("danieljayarajan@gmail.com", "qwertyuiop123")
        }

        Box(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            }
        }
    }
}
package org.gabrieal.gymtracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.gabrieal.gymtracker.util.systemUtil.SpotifyRedirectHandler
import org.gabrieal.gymtracker.util.systemUtil.activityReference
import kotlin.let

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        activityReference = this

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { uri ->
            if (uri.scheme == "gabriealgymtracker" && uri.host == "callback") {
                uri.getQueryParameter("code")?.let {
                    SpotifyRedirectHandler.emitCode(it)
                }
            }
        }
    }
}
package org.gabrieal.gymtracker.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AnimatedImageFromRightVisibility(isVisible: Boolean, resource: DrawableResource) {
    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize().absoluteOffset(y = 4.dp)) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(animationSpec = tween(1000, easing = FastOutSlowInEasing)) { fullWidth -> fullWidth } + fadeIn(),
            exit = slideOutHorizontally(animationSpec = tween(1000, easing = FastOutSlowInEasing)) { fullWidth -> fullWidth } + fadeOut()
        ) {
            Image(
                painter = painterResource(resource),
                contentDescription = resource.toString(),
                modifier = Modifier
                    .size(350.dp)
            )
        }
    }
}

@Composable
fun AnimatedImageFromLeftVisibility(isVisible: Boolean, resource: DrawableResource) {
    Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize().absoluteOffset(y = 4.dp)) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(animationSpec = tween(1000, easing = FastOutSlowInEasing)) { fullWidth -> -fullWidth } + fadeIn(),
            exit = slideOutHorizontally(animationSpec = tween(1000, easing = FastOutSlowInEasing)) { fullWidth -> -fullWidth } + fadeOut()
        ) {
            Image(
                painter = painterResource(resource),
                contentDescription = resource.toString(),
                modifier = Modifier.size(350.dp)
            )
        }
    }
}
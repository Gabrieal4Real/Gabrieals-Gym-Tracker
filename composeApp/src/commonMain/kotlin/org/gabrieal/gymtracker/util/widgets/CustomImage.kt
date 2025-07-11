package org.gabrieal.gymtracker.util.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.colors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AnimatedImage(showImage: Boolean, resource: DrawableResource, isLeft: Boolean): Boolean {
    if (!showImage) {
        return false
    }

    var boxVisibility by rememberSaveable { mutableStateOf(true) }
    var animationVisibility by rememberSaveable { mutableStateOf(false) }
    val scope = MainScope()

    LaunchedEffect(Unit) {
        delay(200)
        animationVisibility = true
    }

    var enterAnimation = slideInHorizontally(
        animationSpec = tween(
            600,
            easing = FastOutSlowInEasing
        )
    ) { fullWidth -> fullWidth } + fadeIn()
    var exitAnimation = slideOutHorizontally(
        animationSpec = tween(
            1000,
            easing = FastOutSlowInEasing
        )
    ) { fullWidth -> fullWidth } + fadeOut()

    if (isLeft) {
        enterAnimation = slideInHorizontally(
            animationSpec = tween(
                600,
                easing = FastOutSlowInEasing
            )
        ) { fullWidth -> -fullWidth } + fadeIn()
        exitAnimation = slideOutHorizontally(
            animationSpec = tween(
                1000,
                easing = FastOutSlowInEasing
            )
        ) { fullWidth -> -fullWidth } + fadeOut()
    }

    if (!boxVisibility) return boxVisibility

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.black.copy(alpha = 0.35f))
            .clickable(
                indication = null,
                interactionSource = null
            ) {
                animationVisibility = false
                scope.launch {
                    delay(200)
                    boxVisibility = false
                }
            }
    ) {
        Box(
            contentAlignment = if (isLeft) Alignment.BottomStart else Alignment.BottomEnd,
            modifier = Modifier.fillMaxSize().absoluteOffset(y = 4.dp)
        ) {
            AnimatedVisibility(
                visible = animationVisibility,
                enter = enterAnimation,
                exit = exitAnimation
            ) {
                Image(
                    painter = painterResource(resource),
                    contentDescription = resource.toString(),
                    modifier = Modifier.size(350.dp)
                )
            }
        }
    }

    return boxVisibility
}

@Composable
fun IconNext() {
    Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
        contentDescription = null,
        tint = colors.textPrimary,
        modifier = Modifier.clip(CircleShape).background(colors.textPrimary.copy(alpha = 0.2f))
    )
}

@Composable
fun RotatingExpandIcon(expanded: Boolean) {
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "IconRotation")

    Icon(
        imageVector = Icons.Rounded.KeyboardArrowDown,
        contentDescription = if (expanded) "Collapse" else "Expand",
        tint = colors.textPrimary,
        modifier = Modifier.rotate(rotation).clip(CircleShape).background(colors.textPrimary.copy(alpha = 0.2f))
    )
}
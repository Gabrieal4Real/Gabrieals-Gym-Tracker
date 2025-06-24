package org.gabrieal.gymtracker.util.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.card_background
import org.gabrieal.gymtracker.colors
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomCard(
    enabled: Boolean,
    onClick: (() -> Unit)? = null,
    backgroundEnabled: Boolean = true,
    isAnimated: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = if (enabled) colors.cardBackground else colors.maroon),
        border = BorderStroke(
            2.dp,
            if (!isAnimated) colors.borderStroke else Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (enabled && onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .then(
                if (isAnimated) {
                    Modifier.drawAnimatedBorder(shape = RoundedCornerShape(8.dp), brush = {
                        Brush.sweepGradient(
                            listOf(
                                colors.slightlyDarkerLinkBlue,
                                colors.borderStroke,
                                colors.textPrimary,
                                colors.borderStroke,
                                colors.slightlyDarkerLinkBlue
                            )
                        )
                    })
                } else {
                    Modifier
                }
            )
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = colors.black,
                spotColor = colors.black
            ),
        content = {
            Box {
                Image(
                    painter = painterResource(Res.drawable.card_background),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize().blur(0.2.dp),
                    alpha = if (backgroundEnabled) 1f else 5f
                )

                content.invoke(this@Card)
            }
        }
    )
}

fun Modifier.drawAnimatedBorder(
    strokeWidth: Dp = 2.dp,
    shape: Shape = CircleShape,
    durationMillis: Int = 4000,
    brush: (Size) -> Brush = {
        Brush.sweepGradient(
            listOf(
                Color.Red, Color.Magenta, Color.Blue,
                Color.Cyan, Color.Green, Color.Yellow,
                Color.Red
            )
        )
    },
) = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Modifier
        .clip(shape)
        .drawWithCache {
            val strokeWidthPx = strokeWidth.toPx()
            val outline: Outline = shape.createOutline(size, layoutDirection, this)
            onDrawWithContent {
                drawContent()

                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = this.saveLayer(null, null)
                    drawOutline(
                        outline = outline,
                        color = Color.Gray,
                        style = Stroke(strokeWidthPx * 2)
                    )
                    rotate(angle) {
                        drawCircle(
                            brush = brush(size),
                            radius = size.width,
                            blendMode = BlendMode.SrcIn,
                        )
                    }
                    this.restoreToCount(checkPoint)
                }
            }
        }
}
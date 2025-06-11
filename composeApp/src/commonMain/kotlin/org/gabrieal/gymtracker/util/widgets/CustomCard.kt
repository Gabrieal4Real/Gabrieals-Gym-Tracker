package org.gabrieal.gymtracker.util.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.card_background
import org.gabrieal.gymtracker.colors
import org.jetbrains.compose.resources.painterResource


@Composable
fun CustomCard(
    enabled: Boolean,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
    backgroundEnabled: Boolean = true
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = if (enabled) colors.cardBackground else colors.maroon),
        border = BorderStroke(2.dp, colors.borderStroke),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (enabled && onClick != null) {
                    Modifier.clickable(onClick = onClick)
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
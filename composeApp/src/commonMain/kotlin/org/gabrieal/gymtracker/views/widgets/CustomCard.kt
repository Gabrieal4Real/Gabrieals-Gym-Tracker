package org.gabrieal.gymtracker.views.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.util.appUtil.Colors


@Composable
fun CustomCard(enabled: Boolean, onClick: (() -> Unit)? = null, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = if (enabled) Colors.CardBackground else Colors.Maroon,
        border = BorderStroke(2.dp, Colors.BorderStroke),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
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
                ambientColor = Colors.Black,
                spotColor = Colors.Black
            ),
        content = content
    )
}
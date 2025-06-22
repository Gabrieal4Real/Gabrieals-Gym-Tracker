package org.gabrieal.gymtracker.util.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis.ItemPlacer.Companion.count
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.multiplatform.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer.Point
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.Position
import com.patrykandpatrick.vico.multiplatform.common.component.LineComponent
import com.patrykandpatrick.vico.multiplatform.common.component.ShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import com.patrykandpatrick.vico.multiplatform.common.shape.DashedShape
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.util.app.RegularText


@Composable
fun CustomLineChart(points: List<Double>, modifier: Modifier = Modifier) {
    val textStyle = TextStyle(
        fontFamily = RegularText(),
        lineHeight = 18.sp,
        color = colors.white,
        fontSize = 12.sp
    )

    val guideline = LineComponent(
        fill = Fill(colors.placeholderColor),
        shape = DashedShape(
            dashLength = 8.dp,
            gapLength = 4.dp
        )
    )

    val lineComponent = LineComponent(
        fill = Fill(colors.borderStroke),
        thickness = 2.5.dp
    )

    CartesianChartHost(
        modifier = modifier,
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.Line(
                        fill = LineCartesianLayer.LineFill.single(Fill(colors.linkBlue)),
                        stroke = LineCartesianLayer.LineStroke.Continuous(thickness = 2.dp),
                        areaFill = LineCartesianLayer.AreaFill.single(
                            Fill(colors.linkBlue.copy(alpha = 0.15f))
                        ),
                        pointConnector = LineCartesianLayer.PointConnector.cubic(),
                        pointProvider = LineCartesianLayer.PointProvider.single(
                            point = Point(
                                component = ShapeComponent(
                                    shape = CorneredShape.Pill,
                                    fill = Fill(colors.white),
                                    strokeFill = Fill(colors.slightlyDarkerLinkBlue),
                                    strokeThickness = 2.5.dp
                                ),
                                12.dp
                            )
                        ),
                        dataLabel = TextComponent(textStyle),
                        dataLabelPosition = Position.Vertical.Bottom,
                    )
                )
            ),
            layerPadding = {
                CartesianLayerPadding(
                    unscalableStart = 12.dp,
                    unscalableEnd = 12.dp
                )
            },
            startAxis = VerticalAxis.rememberStart(
                line = lineComponent,
                label = TextComponent(
                    textStyle,
                    minWidth = TextComponent.MinWidth.fixed(48.dp),
                ),
                guideline = guideline,
                itemPlacer = count(count = { (6) })
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                line = lineComponent,
                label = TextComponent(textStyle),
                guideline = guideline,
            ),
        ),
        model = CartesianChartModel(
            LineCartesianLayerModel.build {
                series(points)
            },
        ),
    )
}
package af.amir.cryptotracker.crypto.presentation.coin_detail.component


import af.amir.cryptotracker.core.presentation.toString
import af.amir.cryptotracker.crypto.presentation.coin_detail.OhlcChartState
import af.amir.cryptotracker.crypto.presentation.coin_detail.OhlcDayRange
import af.amir.cryptotracker.crypto.presentation.components.ErrorButton
import android.text.Layout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.candlestickSeries
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.text.DecimalFormat
import kotlin.math.ceil
import kotlin.math.floor


@Composable
fun OhlcChartContainer(
    state: OhlcChartState,
    daysRange: OhlcDayRange,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    onTimeRangeChange: (OhlcDayRange) -> Unit,
) {

        val context = LocalContext.current

        LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(OhlcDayRange.entries) { ohlcDayRange ->
                FilterChip(
                    selected = ohlcDayRange == daysRange,
                    onClick = { onTimeRangeChange(ohlcDayRange) },
                    label = { Text("${ohlcDayRange.range}D") },
                    enabled = state !is OhlcChartState.Loading
                )
            }
        }

        when (state) {
            is OhlcChartState.Error -> ErrorButton(
                error = state.error.toString(context),
                onClick = onRetry
            )

            OhlcChartState.Loading -> Box(
                Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is OhlcChartState.Content -> OhlcChart(
                state = state,
                daysRange = daysRange,
            )
        }

}


@Composable
private fun OhlcChart(
    state: OhlcChartState.Content,
    daysRange: OhlcDayRange,
    modifier: Modifier = Modifier,
) {

    val modelProducer = remember { CartesianChartModelProducer() }

    val timeUnit =
        remember(daysRange) { resolveTimeUnit(daysRange.range) }
    val vicoData = remember(timeUnit, state.ohlcPoints) {
        state.ohlcPoints.toVicoData(timeUnit)
    }

    val minPrice = vicoData.low.minOrNull() ?: 0.0
    val maxPrice = vicoData.high.maxOrNull() ?: 0.0
    val priceRange = (maxPrice - minPrice)
    val yStep = calculateYStep(priceRange)
    val rangeProvider =
        object : CartesianLayerRangeProvider {
            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                yStep * floor(minY / yStep)

            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
                yStep * ceil(maxY / yStep)
        }
    val startAxisItemPlacer = VerticalAxis.ItemPlacer.step({ yStep })
    val startAxisValueFormatter = CartesianValueFormatter.decimal(priceFormatter(priceRange))

    val markerValueFormatter =
        DefaultCartesianMarker.ValueFormatter.default(priceFormatter(priceRange))


    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            candlestickSeries(
                vicoData.x,
                vicoData.opening,
                vicoData.closing,
                vicoData.low,
                vicoData.high
            )
        }
    }

    CartesianChartHost(
        rememberCartesianChart(
            rememberCandlestickCartesianLayer(rangeProvider = rangeProvider),
            startAxis =
            VerticalAxis.rememberStart(
                valueFormatter = startAxisValueFormatter,
                itemPlacer = startAxisItemPlacer,
            ),
            bottomAxis =
            HorizontalAxis.rememberBottom(
                guideline = null,
                valueFormatter = TimeAxisFormatter(timeUnit)
            ),
            marker = rememberMarker(valueFormatter = markerValueFormatter, showIndicator = false),
        ),
        modelProducer,
        modifier.height(240.dp),
    )
}


private fun calculateYStep(range: Double): Double {
    return when {
        range < 0.01 -> 0.001
        range < 0.1 -> 0.01
        range < 1 -> 0.05
        range < 10 -> 0.5
        range < 100 -> 5.0
        range < 1000 -> 50.0
        else -> 100.0
    }
}


fun priceFormatter(range: Double): DecimalFormat {
    return when {
        range < 0.01 -> DecimalFormat("$0.000")
        range < 0.1 -> DecimalFormat("$0.00")
        range < 1 -> DecimalFormat("$0.00")
        range < 10 -> DecimalFormat("$0.0")
        range < 100 -> DecimalFormat("$#,##0")
        else -> DecimalFormat("$#,###")
    }
}


@Composable
internal fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter =
        DefaultCartesianMarker.ValueFormatter.default(),
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = markerCorneredShape(CorneredShape.Corner.Rounded)
    val labelBackground =
        rememberShapeComponent(
            fill = fill(MaterialTheme.colorScheme.background),
            shape = labelBackgroundShape,
            strokeThickness = 1.dp,
            strokeFill = fill(MaterialTheme.colorScheme.outline),
        )
    val label =
        rememberTextComponent(
            color = MaterialTheme.colorScheme.onSurface,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            padding = insets(8.dp, 4.dp),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
        )
    val indicatorFrontComponent =
        rememberShapeComponent(fill(MaterialTheme.colorScheme.surface), CorneredShape.Pill)
    val guideline = rememberAxisGuidelineComponent()
    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator =
        if (showIndicator) {
            { color ->
                LayeredComponent(
                    back = ShapeComponent(fill(color.copy(alpha = 0.15f)), CorneredShape.Pill),
                    front =
                    LayeredComponent(
                        back = ShapeComponent(fill = fill(color), shape = CorneredShape.Pill),
                        front = indicatorFrontComponent,
                        padding = insets(5.dp),
                    ),
                    padding = insets(10.dp),
                )
            }
        } else {
            null
        },
        indicatorSize = 36.dp,
        guideline = guideline,
    )
}

package org.nsh07.pomodoro.ui.statsScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.VicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.VicoZoomState
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.fill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.nsh07.pomodoro.data.Stat
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.cardShape

/**
 * Cumulative focus chart: running total of focus minutes over the chosen period.
 * Helps users see their overall progress trend.
 */
@Composable
fun CumulativeFocusChart(
    stats: List<Stat?>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val scrollState = rememberVicoScrollState()
    val zoomState = rememberVicoZoomState()

    val nonNull = remember(stats) { stats.filterNotNull().reversed() }

    LaunchedEffect(nonNull) {
        if (nonNull.isEmpty()) return@LaunchedEffect
        withContext(Dispatchers.Default) {
            var cumulative = 0L
            val values = nonNull.map { stat ->
                cumulative += stat.totalFocusTime()
                cumulative / 60_000.0  // convert ms to minutes as Double
            }
            modelProducer.runTransaction {
                lineSeries { series(values) }
            }
        }
    }

    val totalMinutes = nonNull.sumOf { it.totalFocusTime() } / 60_000L

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.surfaceVariant, cardShape)
            .padding(16.dp)
    ) {
        Column {
            Text("Cumulative Focus", style = typography.titleMedium, color = colorScheme.onSurface)
            Text(
                "${totalMinutes}m total over ${nonNull.size} days",
                style = typography.labelSmall,
                color = colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(12.dp))
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer()
                ),
                modelProducer = modelProducer,
                scrollState = scrollState,
                zoomState = zoomState,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
        }
    }
}

package org.nsh07.pomodoro.ui.statsScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import org.nsh07.pomodoro.data.Stat
import org.nsh07.pomodoro.data.StatTime
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.cardShape

/**
 * Hourly productivity heatmap: maps the 4 daily quarters (Q1=midnight-noon,
 * Q2=noon-4pm, Q3=4pm-8pm, Q4=8pm-midnight) to time-of-day productivity tiles.
 */
@Composable
fun HourlyHeatmapChart(
    stats: List<Stat?>,
    modifier: Modifier = Modifier
) {
    val nonNull = remember(stats) { stats.filterNotNull() }
    if (nonNull.isEmpty()) return

    // Average focus per quarter across all recorded days
    val avgQ1 = nonNull.map { it.focusTimeQ1 }.average()
    val avgQ2 = nonNull.map { it.focusTimeQ2 }.average()
    val avgQ3 = nonNull.map { it.focusTimeQ3 }.average()
    val avgQ4 = nonNull.map { it.focusTimeQ4 }.average()
    val maxAvg = maxOf(avgQ1, avgQ2, avgQ3, avgQ4).coerceAtLeast(1.0)

    val quarters = listOf(
        "12am–12pm" to avgQ1,
        "12pm–4pm" to avgQ2,
        "4pm–8pm" to avgQ3,
        "8pm–12am" to avgQ4
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.surfaceVariant, cardShape)
            .padding(16.dp)
    ) {
        Column {
            Text("Peak Focus Hours", style = typography.titleMedium, color = colorScheme.onSurface)
            Text(
                "Average focus per time of day",
                style = typography.labelSmall,
                color = colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                quarters.forEach { (label, avg) ->
                    val alpha = (avg / maxAvg).toFloat().coerceIn(0.15f, 1f)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .alpha(alpha)
                                .background(colorScheme.primary, RoundedCornerShape(12.dp))
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            label,
                            style = typography.labelSmall,
                            color = colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Darker = more focus time",
                style = typography.labelSmall,
                color = colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

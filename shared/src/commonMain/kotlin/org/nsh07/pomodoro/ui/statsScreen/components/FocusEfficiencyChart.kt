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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.nsh07.pomodoro.data.Stat
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.cardShape
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.segmentedListItemShapes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import kotlin.math.max

/**
 * Focus efficiency chart: shows each day's focus-to-total-time ratio
 * as a percentage bar for the last 7 days.
 */
@Composable
fun FocusEfficiencyChart(
    stats: List<Stat?>,
    modifier: Modifier = Modifier
) {
    val displayStats = remember(stats) {
        stats.filterNotNull().take(7).reversed()
    }
    if (displayStats.isEmpty()) return

    val maxMs = remember(displayStats) {
        displayStats.maxOf { it.totalFocusTime() + it.breakTime }.coerceAtLeast(1L)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.surfaceVariant, cardShape)
            .padding(16.dp)
    ) {
        Column {
            Text("Focus Efficiency", style = typography.titleMedium, color = colorScheme.onSurface)
            Text(
                "Focus vs break time per day",
                style = typography.labelSmall,
                color = colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            ) {
                displayStats.forEach { stat ->
                    val total = stat.totalFocusTime() + stat.breakTime
                    val focusFraction = if (total > 0) stat.totalFocusTime().toFloat() / total else 0f
                    val barHeightFraction = if (maxMs > 0) total.toFloat() / maxMs else 0f
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .fillMaxHeight(barHeightFraction.coerceAtLeast(0.04f)),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                // Break portion
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight((1f - focusFraction).coerceAtLeast(0.01f))
                                        .background(
                                            colorScheme.tertiaryContainer,
                                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                        )
                                )
                                // Focus portion
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(focusFraction.coerceAtLeast(0.01f))
                                        .background(colorScheme.primary)
                                )
                            }
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            stat.date.dayOfWeek.name.take(2),
                            style = typography.labelSmall,
                            color = colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.width(12.dp).height(12.dp).background(colorScheme.primary, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(4.dp))
                    Text("Focus", style = typography.labelSmall, color = colorScheme.onSurface.copy(alpha = 0.7f))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.width(12.dp).height(12.dp).background(colorScheme.tertiaryContainer, RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(4.dp))
                    Text("Break", style = typography.labelSmall, color = colorScheme.onSurface.copy(alpha = 0.7f))
                }
            }
        }
    }
}

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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.nsh07.pomodoro.data.Stat
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.cardShape
import tomato.shared.generated.resources.Res
import tomato.shared.generated.resources.bolt

/**
 * Displays the user's current focus streak (consecutive days with focus > 0)
 * and their longest streak ever.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StreakCard(
    stats: List<Stat?>,
    modifier: Modifier = Modifier
) {
    val nonNullStats = stats.filterNotNull()
    
    // Current streak: count from most recent day backwards
    val currentStreak = run {
        var streak = 0
        for (stat in nonNullStats) {
            if (stat.totalFocusTime() > 0) streak++ else break
        }
        streak
    }
    
    // Longest streak
    val longestStreak = run {
        var best = 0
        var current = 0
        for (stat in nonNullStats) {
            if (stat.totalFocusTime() > 0) {
                current++
                if (current > best) best = current
            } else {
                current = 0
            }
        }
        best
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.primaryContainer, cardShape)
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(Res.drawable.bolt),
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        "Focus Streak",
                        style = typography.titleMedium,
                        color = colorScheme.onPrimaryContainer
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "$currentStreak days",
                    style = typography.displaySmall,
                    color = colorScheme.primary
                )
                Text(
                    "current streak",
                    style = typography.labelMedium,
                    color = colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$longestStreak",
                    style = typography.headlineLarge,
                    color = colorScheme.secondary
                )
                Text(
                    "best streak",
                    style = typography.labelMedium,
                    color = colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

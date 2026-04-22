package org.nsh07.pomodoro.ui.settingsScreen.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.nsh07.pomodoro.ui.mergePaddingValues
import org.nsh07.pomodoro.ui.settingsScreen.viewModel.SettingsAction
import org.nsh07.pomodoro.ui.settingsScreen.viewModel.SettingsState
import org.nsh07.pomodoro.ui.theme.CustomColors.detailPaneTopBarColors
import org.nsh07.pomodoro.ui.theme.CustomColors.listItemColors
import org.nsh07.pomodoro.ui.theme.CustomColors.switchColors
import org.nsh07.pomodoro.ui.theme.CustomColors.topBarColors
import org.nsh07.pomodoro.ui.theme.LocalAppFonts
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.PANE_MAX_WIDTH
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.segmentedListItemShapes
import tomato.shared.generated.resources.Res
import tomato.shared.generated.resources.arrow_back
import tomato.shared.generated.resources.back
import tomato.shared.generated.resources.clocks
import tomato.shared.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ClockCustomizationSettings(
    settingsState: SettingsState,
    contentPadding: PaddingValues,
    onAction: (SettingsAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val widthExpanded = currentWindowAdaptiveInfo()
        .windowSizeClass
        .isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
    val barColors = if (widthExpanded) detailPaneTopBarColors else topBarColors

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(barColors.containerColor)
    ) {
        Scaffold(
            topBar = {
                LargeFlexibleTopAppBar(
                    title = {
                        Text("Clock & Display", fontFamily = LocalAppFonts.current.topBarTitle)
                    },
                    subtitle = { Text(stringResource(Res.string.settings)) },
                    navigationIcon = {
                        if (!widthExpanded)
                            FilledTonalIconButton(
                                onClick = onBack,
                                shapes = IconButtonDefaults.shapes(),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = listItemColors.containerColor
                                )
                            ) {
                                Icon(painterResource(Res.drawable.arrow_back), stringResource(Res.string.back))
                            }
                    },
                    colors = barColors,
                    scrollBehavior = scrollBehavior
                )
            },
            containerColor = barColors.containerColor,
            modifier = modifier.widthIn(max = PANE_MAX_WIDTH).nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            val insets = mergePaddingValues(innerPadding, contentPadding)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = insets,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                item { Spacer(Modifier.height(14.dp)) }

                // ─ Clock Style ──────────────────────────────────────────────────
                item {
                    Text(
                        "Clock Style",
                        style = typography.labelLarge,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                }
                val clockStyles = listOf("digital" to "Digital", "minimal" to "Minimal", "bold" to "Bold")
                clockStyles.forEachIndexed { idx, (key, label) ->
                    item {
                        SegmentedListItem(
                            onClick = { onAction(SettingsAction.SaveClockStyle(key)) },
                            content = { Text(label) },
                            supportingContent = {
                                Text(
                                    when (key) {
                                        "digital" -> "Standard digital clock display"
                                        "minimal" -> "Clean minimal look, no extra decoration"
                                        "bold" -> "Large bold numerals for at-a-glance reading"
                                        else -> ""
                                    }
                                )
                            },
                            trailingContent = {
                                if (settingsState.clockStyle == key) {
                                    Icon(
                                        painterResource(Res.drawable.back),
                                        contentDescription = null,
                                        tint = colorScheme.primary
                                    )
                                }
                            },
                            selected = settingsState.clockStyle == key,
                            shapes = segmentedListItemShapes(idx, clockStyles.size),
                            colors = listItemColors
                        )
                    }
                }

                item { Spacer(Modifier.height(14.dp)) }

                // ─ Clock Font Weight ────────────────────────────────────────────
                item {
                    Text(
                        "Number Style",
                        style = typography.labelLarge,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                }
                val fontWeights = listOf("light" to "Light", "normal" to "Regular", "heavy" to "Heavy")
                fontWeights.forEachIndexed { idx, (key, label) ->
                    item {
                        SegmentedListItem(
                            onClick = { onAction(SettingsAction.SaveClockFontWeight(key)) },
                            content = {
                                Text(
                                    label,
                                    fontWeight = when (key) {
                                        "light" -> FontWeight.Light
                                        "heavy" -> FontWeight.ExtraBold
                                        else -> FontWeight.Normal
                                    }
                                )
                            },
                            selected = settingsState.clockFontWeight == key,
                            shapes = segmentedListItemShapes(idx, fontWeights.size),
                            colors = listItemColors
                        )
                    }
                }

                item { Spacer(Modifier.height(14.dp)) }

                // ─ Compact Mode ─────────────────────────────────────────────────
                item {
                    SegmentedListItem(
                        onClick = { onAction(SettingsAction.SaveCompactMode(!settingsState.compactMode)) },
                        leadingContent = {
                            Icon(painterResource(Res.drawable.clocks), null)
                        },
                        content = { Text("Compact Mode") },
                        supportingContent = { Text("Reduce spacing on the timer screen") },
                        trailingContent = {
                            Switch(
                                checked = settingsState.compactMode,
                                onCheckedChange = { onAction(SettingsAction.SaveCompactMode(it)) },
                                colors = switchColors
                            )
                        },
                        selected = settingsState.compactMode,
                        shapes = segmentedListItemShapes(0, 1),
                        colors = listItemColors
                    )
                }

                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

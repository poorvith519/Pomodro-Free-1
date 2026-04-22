package org.nsh07.pomodoro.ui.settingsScreen.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.nsh07.pomodoro.ui.mergePaddingValues
import org.nsh07.pomodoro.ui.settingsScreen.viewModel.SettingsAction
import org.nsh07.pomodoro.ui.settingsScreen.viewModel.SettingsState
import org.nsh07.pomodoro.ui.theme.CustomColors.detailPaneTopBarColors
import org.nsh07.pomodoro.ui.theme.CustomColors.listItemColors
import org.nsh07.pomodoro.ui.theme.CustomColors.topBarColors
import org.nsh07.pomodoro.ui.theme.LocalAppFonts
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.PANE_MAX_WIDTH
import org.nsh07.pomodoro.ui.theme.TomatoShapeDefaults.segmentedListItemShapes
import tomato.shared.generated.resources.Res
import tomato.shared.generated.resources.arrow_back
import tomato.shared.generated.resources.back
import tomato.shared.generated.resources.palette
import tomato.shared.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WidgetThemeSettings(
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
                        Text("Widgets & Theme", fontFamily = LocalAppFonts.current.topBarTitle)
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

                // ─ Widget Style ─────────────────────────────────────────────────
                item {
                    Text(
                        "Widget Layout",
                        style = typography.labelLarge,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                }
                val widgetStyles = listOf(
                    "standard" to Pair("Standard", "Default timer widget with progress ring"),
                    "compact" to Pair("Compact", "Smaller widget — fits in tight home screen spaces"),
                    "bold" to Pair("Bold", "Large text-only widget, easy to read at a glance")
                )
                widgetStyles.forEachIndexed { idx, (key, labelDesc) ->
                    val (label, desc) = labelDesc
                    item {
                        SegmentedListItem(
                            onClick = { onAction(SettingsAction.SaveWidgetStyle(key)) },
                            content = { Text(label) },
                            supportingContent = { Text(desc) },
                            trailingContent = {
                                if (settingsState.widgetStyle == key)
                                    Icon(
                                        painterResource(Res.drawable.palette),
                                        null,
                                        tint = colorScheme.primary
                                    )
                            },
                            selected = settingsState.widgetStyle == key,
                            shapes = segmentedListItemShapes(idx, widgetStyles.size),
                            colors = listItemColors
                        )
                    }
                }

                item { Spacer(Modifier.height(14.dp)) }

                // ─ Card Corner Radius ────────────────────────────────────────────
                item {
                    Text(
                        "Card Corner Radius",
                        style = typography.labelLarge,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                }
                val radii = listOf(
                    "small" to Pair("Sharp", "Minimal rounding for a modern edge"),
                    "medium" to Pair("Rounded", "Default Material You rounding"),
                    "large" to Pair("Pill", "Maximum rounding for a soft look")
                )
                radii.forEachIndexed { idx, (key, labelDesc) ->
                    val (label, desc) = labelDesc
                    item {
                        SegmentedListItem(
                            onClick = { onAction(SettingsAction.SaveCardRadius(key)) },
                            content = { Text(label) },
                            supportingContent = { Text(desc) },
                            selected = settingsState.cardRadius == key,
                            shapes = segmentedListItemShapes(idx, radii.size),
                            colors = listItemColors
                        )
                    }
                }

                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

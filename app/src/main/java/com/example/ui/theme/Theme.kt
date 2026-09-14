package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = FinancialBlueDark,
    onPrimary = SlateBackgroundDark,
    primaryContainer = FinancialBlueDarkContainer,
    onPrimaryContainer = FinancialBlueDark,
    secondary = FinancialTeal,
    onSecondary = SlateBackgroundDark,
    secondaryContainer = FinancialTealOnContainer,
    background = SlateBackgroundDark,
    surface = SlateSurfaceDark,
    surfaceVariant = SlateSurfaceVariantDark,
    outline = SlateTextSecondary,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = FinancialBluePrimary,
    onPrimary = FinancialBlueOnPrimary,
    primaryContainer = FinancialBlueContainer,
    onPrimaryContainer = FinancialBlueOnContainer,
    secondary = FinancialTeal,
    onSecondary = FinancialBlueOnPrimary,
    secondaryContainer = FinancialTealContainer,
    onSecondaryContainer = FinancialTealOnContainer,
    tertiary = FinancialAmber,
    tertiaryContainer = FinancialAmberContainer,
    background = SlateBackground,
    surface = SlateSurface,
    surfaceVariant = SlateSurfaceVariant,
    outline = SlateOutline,
    outlineVariant = SlateOutlineVariant,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

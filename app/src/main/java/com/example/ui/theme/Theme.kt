package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

data class TimeMetrics(
    val bloomIntensity: Float,
    val glassAlpha: Float,
    val surfaceAlpha: Float
)

val LocalTimeMetrics = staticCompositionLocalOf { TimeMetrics(1f, 0.1f, 0.05f) }

private val DarkColorScheme =
  darkColorScheme(
    primary = PrimaryElectricIndigo,
    secondary = SecondaryAmber,
    background = DeepSpace,
    surface = GlassSurface,
    onPrimary = TextLight,
    onSecondary = DeepSpace,
    onBackground = TextLight,
    onSurface = TextLight,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PrimaryElectricIndigo,
    secondary = SecondaryAmber,
    background = LightGlass,
    surface = GlassSurface,
    onPrimary = TextLight,
    onSecondary = DeepSpace,
    onBackground = DeepSpace,
    onSurface = DeepSpace,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark theme for cinematic effect, or isSystemInDarkTheme()
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false, // Disable dynamic colors to keep Bioluminescent theme
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

  val timeMetrics = remember {
      val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
      val minute = Calendar.getInstance().get(Calendar.MINUTE)
      val timeInHours = hour + minute / 60f
      
      // Sun intensity: -1 at midnight, 1 at noon
      val sunIntensity = kotlin.math.sin((timeInHours - 6f) / 12f * Math.PI).toFloat()
      
      // Normalized 0 to 1 where 0 is midnight, 1 is noon
      val normalizedTime = (sunIntensity + 1f) / 2f
      
      // Bloom: Brighter in day (1.5x), lower at night (0.5x)
      val bloom = 0.5f + (1.0f * normalizedTime) 
      
      // Glass alpha: more opaque in day (0.2), more transparent at night (0.05)
      val glass = 0.05f + (0.15f * normalizedTime)
      
      // Surface alpha: more opaque in day (0.1), more transparent at night (0.03)
      val surface = 0.03f + (0.07f * normalizedTime)
      
      TimeMetrics(bloomIntensity = bloom, glassAlpha = glass, surfaceAlpha = surface)
  }

  CompositionLocalProvider(
      LocalTimeMetrics provides timeMetrics
  ) {
      MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
  }
}


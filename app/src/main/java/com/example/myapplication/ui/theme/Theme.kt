package com.example.myapplication.ui.theme
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext



private val lightScheme = lightColorScheme(

    /**
     * Colores de la app
     * Estos funcionan de esta manera
     * primary = Color primario de la app
     * onPrimary = Color del texto del boton primario (Va acompañando a primary)
     *
     * Lo normal es que color y su "on" vayan juntos porque hacen el contraste necesario, pero
     * se puede modificar si el color no nos convence o necesitamos otro color.
     */


    // Color principal de la app
    primary = primaryLight,
    onPrimary = onPrimaryLight,

    // Color principal para el fondo
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,

    // Color secundario de la app
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,

    // Color secundario para el fondo
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,

    // Color terciario de la app (no recomendable)
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,

    // Color terciario para el fondo (no recomendable)
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,

    // Color de errores
    error = errorLight,
    onError = onErrorLight,

    // Color de errores para el fondo
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,

    // Color de fondo general para la app
    background = backgroundLight,
    onBackground = onBackgroundLight,

    // Color de fondo (SUPERFICIES)
    surface = surfaceLight,
    onSurface = onSurfaceLight,

    // Color de fondo alternativo (DIVIDERS)
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,

    // Color delineado
    outline = outlineLight,
    outlineVariant = outlineVariantLight,

    // Color sombra
    scrim = scrimLight,

    // Colores de superficie invertidos
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    surfaceDim = surfaceDimLight,

    // Color primario invertido
    inversePrimary = inversePrimaryLight,

    // Niveles de elevación del color para el surface
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(

    /**
     * Colores de la app (MODO OSCURO)
     * Funcionan igual que en el tema claro, pero adaptados
     * para fondos oscuros y menor fatiga visual.
     *
     * Igual que en lightTheme, cada color tiene su "onColor"
     * que define el color del texto o iconos que van encima.
     */

    // Color principal de la app en modo oscuro
    primary = primaryDark,
    onPrimary = onPrimaryDark,

    // Color principal para fondos destacados en modo oscuro
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,

    // Color secundario de la app en modo oscuro
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,

    // Color secundario para fondos en modo oscuro
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,

    // Color terciario de la app en modo oscuro (no recomendable)
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,

    // Color terciario para fondos en modo oscuro (no recomendable)
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,

    // Color de errores en modo oscuro
    error = errorDark,
    onError = onErrorDark,

    // Color de errores para fondos en modo oscuro
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,

    // Color de fondo general de la app en modo oscuro
    background = backgroundDark,
    onBackground = onBackgroundDark,

    // Color de fondo de superficies (cards, dialogs, sheets)
    surface = surfaceDark,
    onSurface = onSurfaceDark,

    // Color de fondo alternativo (DIVIDERS, listas)
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,

    // Color para líneas, bordes y divisores
    outline = outlineDark,
    outlineVariant = outlineVariantDark,

    // Color de sombra para modales y dialogs
    scrim = scrimDark,

    // Colores de superficie invertidos (ej. bottom sheets)
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,

    // Color primario invertido (usado sobre inverseSurface)
    inversePrimary = inversePrimaryDark,

    // Superficie más oscura para jerarquía visual
    surfaceDim = surfaceDimDark,

    // Superficie más clara dentro del modo oscuro
    surfaceBright = surfaceBrightDark,

    // Niveles de elevación del color para el surface en modo oscuro
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    accesible: Boolean = false,
    fontSizeOption: String = "Normal",
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      
      darkTheme -> darkScheme
      else -> lightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = getAppTypography(accesible, fontSizeOption),
    content = content
  )
}


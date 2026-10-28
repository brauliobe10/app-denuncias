package com.bellodas.denunciasapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AzulElectrico,
    onPrimary = Blanco,
    primaryContainer = AzulInstitucional,
    onPrimaryContainer = Blanco,
    secondary = NaranjaVibrante,
    onSecondary = Blanco,
    background = GrisAzuladoClaro,
    onBackground = AzulGrisaceoOscuro,
    surface = Blanco,
    onSurface = AzulGrisaceoOscuro,
    error = RojoError,
    onError = Blanco
)

@Composable
fun DenunciasTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

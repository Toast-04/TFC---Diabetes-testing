package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import com.example.myapplication.R

// Fonts declaration
val Lato = FontFamily(
    Font(R.font.lato),
    Font(R.font.lato_negrita))

val Lexend = FontFamily(
    Font(R.font.lexend),
    Font(R.font.lexend_negrita))

// Fonts styles
val NormalTypography = Typography(
    bodySmall = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
)

val AccesibleTypography = Typography(
    bodySmall = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Lexend,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
)

// Default Material 3 typography values
val baseline = Typography()

fun getAppTypography(accesible: Boolean, fontSizeOption: String): Typography {
    val fontFamily = if (accesible) Lexend else Lato

    // Ajuste de tamaño según opción
    val sizeMap = when (fontSizeOption) {
        "Pequeña" -> mapOf("small" to 12.sp, "medium" to 14.sp, "large" to 16.sp)
        "Grande" -> mapOf("small" to 16.sp, "medium" to 18.sp, "large" to 20.sp)
        else -> mapOf("small" to 14.sp, "medium" to 16.sp, "large" to 18.sp) // Normal
    }

    return Typography(
        bodySmall = TextStyle(fontFamily = fontFamily, fontSize = sizeMap["small"]!!),
        bodyMedium = TextStyle(fontFamily = fontFamily, fontSize = sizeMap["medium"]!!),
        bodyLarge = TextStyle(fontFamily = fontFamily, fontSize = sizeMap["large"]!!),
        labelSmall = TextStyle(fontFamily = fontFamily, fontSize = sizeMap["small"]!!, fontWeight = FontWeight.Bold),
        labelMedium = TextStyle(fontFamily = fontFamily, fontSize = sizeMap["medium"]!!, fontWeight = FontWeight.Bold),
        labelLarge = TextStyle(fontFamily = fontFamily, fontSize = sizeMap["large"]!!, fontWeight = FontWeight.Bold),
    )
}



package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var modoAccesible by remember { mutableStateOf(false) }
            var fontSizeOption by remember { mutableStateOf("Normal") }

            AppTheme(
                darkTheme = isDarkTheme,
                accesible = modoAccesible,
                fontSizeOption = fontSizeOption,
                dynamicColor = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it },
                        modoAccesible = modoAccesible,
                        onModoAccesibleChange = { modoAccesible = it },
                        fontSizeOption = fontSizeOption,
                        onFontSizeChange = { fontSizeOption = it }
                    )
                }
            }
        }
    }
}
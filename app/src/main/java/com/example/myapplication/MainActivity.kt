package com.example.myapplication

import DbHelper
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

        //Iniciamos el Helper y copiamos la DB si es necesario
        val dbHelper = DbHelper(this)
        dbHelper.checkAndCopyDatabase() //<-- Esto muevo el archivo .db a la carpeta correcta

        //Creamos el ViewModel
        val mainViewModel = MainViewModel(dbHelper)

        crearCanalNotificacion(this)
        pedirPermisoNotificaciones(this)


        // Contenido de la app
        setContent {

            // Variables de estado para el tema y el modo accesible
            var isDarkTheme by remember { mutableStateOf(false) }
            var modoAccesible by remember { mutableStateOf(false) }
            var fontSizeOption by remember { mutableStateOf("Normal") }

            // Aplicación con el tema y el modo accesible proporcionados
            AppTheme(
                darkTheme = isDarkTheme,
                accesible = modoAccesible,
                fontSizeOption = fontSizeOption, // * Se puede optimizar *
                dynamicColor = false // No cambiar
            ) {

                // Fondo de la app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Composición de la app *NO TOCAR*
                    MyApp(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it },
                        modoAccesible = modoAccesible,
                        onModoAccesibleChange = { modoAccesible = it },
                        fontSizeOption = fontSizeOption,
                        onFontSizeChange = { fontSizeOption = it },
                        //Pasamos el viewModel a la app
                        viewModel = mainViewModel
                    )
                }
            }
        }
    }
}
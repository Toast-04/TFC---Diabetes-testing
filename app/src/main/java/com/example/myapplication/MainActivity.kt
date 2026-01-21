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

        val factory = MainViewModelFactory(dbHelper, SettingsManager(this))
        //Creamos el ViewModel
        val mainViewModel = MainViewModel(dbHelper, SettingsManager(this))

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
                darkTheme = mainViewModel.isDarkTheme,
                accesible = mainViewModel.isDislexiaMode,
                fontSizeOption = mainViewModel.fontSizeOption, // * Se puede optimizar *
                dynamicColor = false // No cambiar
            ) {

                // Fondo de la app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Composición de la app *NO TOCAR*
                    MyApp(
                        isDarkTheme = mainViewModel.isDarkTheme,
                        onThemeChange = { mainViewModel.updateDarkMode(it) }, // Guarda en disco
                        modoAccesible = mainViewModel.isDislexiaMode,
                        onModoAccesibleChange = { mainViewModel.updateModoDislexia(it) }, // Guarda en disco
                        fontSizeOption = mainViewModel.fontSizeOption,
                        onFontSizeChange = { mainViewModel.updateFontSize(it) }, // Guarda en disco
                        viewModel = mainViewModel
                    )
                }
            }
        }
    }
}
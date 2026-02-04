package com.example.myapplication

import DbHelper
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(
    private val dbHelper: DbHelper,
    private val settingsManager: SettingsManager
    ) : ViewModel() {

    //Estados persistentes para pagina config
    var factorSensibilidad by mutableStateOf("")
    var isDarkTheme by mutableStateOf(false)
    var isDislexiaMode by mutableStateOf(false)
    var fontSizeOption by mutableStateOf("Normal")
    var ratioManana by mutableStateOf("")
    var ratioMediodia by mutableStateOf("")
    var ratioNoche by mutableStateOf("")

    //Variable para gramos mainPant
    var campoGramos by mutableStateOf("")

    //Lista de Tablas (Categorias)
    var listaTablas by mutableStateOf(emptyList<String>())
        private set
    var tablaSeleccionada by mutableStateOf("")
        private set

    //Lista de Elementos de la tabla seleccionada
    var listaAlimentos by mutableStateOf(emptyList<String>())
        private set
    var alimentoSeleccionado by mutableStateOf("")
        private set

    //Lista para opciones de ratio
    val opcionesRatio = listOf("Mañana", "Mediodia", "Noche")

    //Estado para la etiqueta seleccionada
    var ratioSeleccionada by mutableStateOf("")
        private set

    // Estado notificaciones
    var notificationsEnabled by mutableStateOf(false)
        private set

    var notificationHour by mutableStateOf(9)
        private set

    var notificationMinute by mutableStateOf(0)
        private set

    //Variable para guardar HC
    var hcSeleccionado by mutableStateOf(0)
        private set

    //Variable para total acumulado del modo receta
    var totalAcumulado by mutableStateOf(0.0)
        private set


    init {
        cargarTablas()
        //Recogemos todos los valores de DataStore

        viewModelScope.launch {
            settingsManager.factorFlow.collect { factor ->
                factorSensibilidad = factor ?: ""
            }
        }
        viewModelScope.launch {
            settingsManager.darkModeFlow.collect { isDarkTheme = it}
        }
        viewModelScope.launch {
            settingsManager.dislexiaModeFlow.collect { isDislexiaMode = it}
        }
        viewModelScope.launch {
            settingsManager.fontSizeFlow.collect { fontSizeOption = it}
        }
        viewModelScope.launch {
            settingsManager.ratioMananaFlow.collect { manana ->
                ratioManana = manana?: ""
            }
        }
        viewModelScope.launch {
            settingsManager.ratioMediodiaFlow.collect { mediodia ->
                ratioMediodia = mediodia?: ""
            }
        }
        viewModelScope.launch {
            settingsManager.ratioNocheFlow.collect { noche ->
                ratioNoche = noche?: ""
            }
        }

        viewModelScope.launch {
            settingsManager.notificationsEnabledFlow.collect {
                notificationsEnabled = it
            }
        }

        viewModelScope.launch {
            settingsManager.notificationHourFlow.collect {
                notificationHour = it
            }
        }

        viewModelScope.launch {
            settingsManager.notificationMinuteFlow.collect {
                notificationMinute = it
            }
        }


    }

    fun updateFactor(v: String) {factorSensibilidad = v; viewModelScope.launch { settingsManager.saveFactor(v)}}
    fun updateDarkMode(b: Boolean) { isDarkTheme = b; viewModelScope.launch { settingsManager.saveDarkMode(b) } }
    fun updateModoDislexia(b: Boolean) { isDislexiaMode = b; viewModelScope.launch { settingsManager.saveDislexia(b) } }
    fun updateFontSize(s: String) { fontSizeOption = s; viewModelScope.launch { settingsManager.saveFontSize(s) } }
    fun updateRatioManana(v: String) { ratioManana = v; viewModelScope.launch { settingsManager.saveRatioManana(v) } }
    fun updateRatioMediodia(v: String) { ratioMediodia = v; viewModelScope.launch { settingsManager.saveRatioMediodia(v) } }
    fun updateRatioNoche(v: String) { ratioNoche = v; viewModelScope.launch { settingsManager.saveRatioNoche(v) } }
    private fun cargarTablas(){
        viewModelScope.launch(Dispatchers.IO) {
            listaTablas = dbHelper.ObtenerTablas()
        }
    }
    //Funcion para tabla seleccionada
    fun onTablaSeleccionada(tabla: String) {
        tablaSeleccionada = tabla
        alimentoSeleccionado = "" //Reiniciamos el alimento al cambiar de tabla
        listaAlimentos = emptyList() //Reiniciamos la lista de alimentos

        viewModelScope.launch(Dispatchers.IO) {
            listaAlimentos = dbHelper.obtenerElementosTabla(tabla)
        }
    }

    //Funcion para alimento seleccionado
    fun onAlimentoSeleccionado(alimento: String) {
        alimentoSeleccionado = alimento

        // Cada vez que se selecciona un nombre, buscamos su HC en la DB
        viewModelScope.launch(Dispatchers.IO) {
            hcSeleccionado = dbHelper.obtenerHcAlimento(tablaSeleccionada, alimento)
        }
    }

    //Funcion para para la ratio seleccionada
    fun onRatioSeleccionada(etiqueta: String) {
        ratioSeleccionada = etiqueta
    }


    // Funcion notificaciones
    fun updateNotificationsEnabled(enabled: Boolean, context: Context) {
        notificationsEnabled = enabled
        viewModelScope.launch {
            settingsManager.saveNotificationsEnabled(enabled)
        }

        if (!enabled) {
            cancelNotification(context)
        } else {
            scheduleNotification(context, notificationHour, notificationMinute)
        }
    }

    // Fncion para la hora de las notificaciones
    fun updateNotificationTime(context: Context, hour: Int, minute: Int) {
        notificationHour = hour
        notificationMinute = minute

        viewModelScope.launch {
            // Guardamos ambos juntos en DataStore
            settingsManager.saveNotificationTime(hour, minute)
        }

        // Programamos la notificación si está activada
        if (notificationsEnabled) {
            scheduleNotification(context, hour, minute)
        }
    }


    //Propiedad auxiliar para obtener el valor de la ratio segun la seleccion
    val ratioValorActual: String
        get() = when (ratioSeleccionada) {
            "Mañana" -> ratioManana
            "Mediodia" -> ratioMediodia
            "Noche" -> ratioNoche
            else -> ""
        }

    //Funcion para añadir al total acumulado
    fun añadirAlTotal(valor: Double) {
        totalAcumulado += valor
    }

    fun limpiarTotal() {
        totalAcumulado = 0.0
    }

    //Funcion para resetear seleccion al cambiar entre pantallas
    fun resetearSeleccion(){
        tablaSeleccionada = ""
        alimentoSeleccionado = ""
        ratioSeleccionada = ""
        campoGramos = ""
        hcSeleccionado = 0
        totalAcumulado = 0.0
    }

}
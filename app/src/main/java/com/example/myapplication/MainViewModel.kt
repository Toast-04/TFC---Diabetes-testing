package com.example.myapplication

import DbHelper
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

    //Estados persistentes
    var factorSensibilidad by mutableStateOf("")
    var isDarkTheme by mutableStateOf(false)
    var isDislexiaMode by mutableStateOf(false)
    var fontSizeOption by mutableStateOf("Normal")
    var ratioManana by mutableStateOf("")
    var ratioMediodia by mutableStateOf("")
    var ratioNoche by mutableStateOf("")

    //Estado : Lista de Tablas (Categorias)
    var listaTablas by mutableStateOf(emptyList<String>())
        private set
    var tablaSeleccionada by mutableStateOf("")
        private set

    //2do Estado : Lista de Elementos de la tabla seleccionada
    var listaAlimentos by mutableStateOf(emptyList<String>())
        private set
    var alimentoSeleccionado by mutableStateOf("")
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

    fun onTablaSeleccionada(tabla: String) {
        tablaSeleccionada = tabla
        alimentoSeleccionado = "" //Reiniciamos el alimento al cambiar de tabla
        listaAlimentos = emptyList() //Reiniciamos la lista de alimentos

        viewModelScope.launch(Dispatchers.IO) {
            listaAlimentos = dbHelper.obtenerElementosTabla(tabla)
        }
    }

    fun onAlimentoSeleccionado(alimento: String) {
        alimentoSeleccionado = alimento
    }


}





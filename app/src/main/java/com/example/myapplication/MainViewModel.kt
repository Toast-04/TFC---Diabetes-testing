package com.example.myapplication

import DbHelper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val dbHelper: DbHelper) : ViewModel() {

    //1er Estado : Lista de Tablas (Categorias)
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
    }

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





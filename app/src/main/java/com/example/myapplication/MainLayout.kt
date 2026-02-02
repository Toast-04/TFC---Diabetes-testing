package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Layout principal de la app
@Composable
fun MainLayout(modifier: Modifier = Modifier,
               viewModel: MainViewModel //<-- Parametro nuevo para el ViewModel
) {

    var textoResultado by remember { mutableStateOf("") }
    //La variables que aqui habian ahora son innecesarias ya que van en el viewmodel

    // Interior de la pantalla principal
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Pantalla Principal",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //DropDown Principal, selector de clase de alimentos
        OptimizedDropdown(
            value = viewModel.tablaSeleccionada.replace('_', ' '),
            onValueChange = { nuevaTabla ->
                //Cambia los '_' por espacios en el listado de tablas
                val nombreReal = viewModel.listaTablas.find { it.replace('_', ' ') == nuevaTabla }
                if (nombreReal != null) {
                    viewModel.onTablaSeleccionada(nombreReal)
                }
            },
            label = "Seleccionar Categoría",
            options = viewModel.listaTablas.map {it.replace('_', ' ')},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // DropDown Secundario, selector de alimentos
            OptimizedDropdown(
                value = viewModel.alimentoSeleccionado,
                onValueChange = { nuevoAlimento ->
                    viewModel.onAlimentoSeleccionado(nuevoAlimento)
                },
                label = "Seleccionar Alimento",
                options = viewModel.listaAlimentos, // <-- Esto se actualiza solo
                modifier = Modifier.weight(1f),
            )

            // Selector ratio
            OptimizedDropdown(
                value = viewModel.ratioSeleccionada,
                onValueChange = { nuevaRatio ->
                    viewModel.onRatioSeleccionada(nuevaRatio)
                },
                label = "Selecciona el momento del dia",
                options = viewModel.opcionesRatio,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para la cantidad de gramps
        OutlinedTextField(
            value = viewModel.campoGramos,

            // Formato adecuado para dentro de textfield *CAMBIAR SI QUIERES*
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$")))
                    viewModel.campoGramos = it
            },
            placeholder = { Text("Gramos") },
            label = { Text("Gramos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.width(250.dp)
        )

        Spacer(modifier = Modifier.height(72.dp))

        // Caja de resultado
        ResultBox(textoResultado)


        Spacer(modifier = Modifier.weight(0.8f))

        // Boton para hacer la operacion
        Button(
            onClick = {
                // USAMOS EL HC QUE YA BUSCÓ EL VIEWMODEL
                val alimentoHC = viewModel.hcSeleccionado

                // USAMOS EL VALOR NUMÉRICO DEL RATIO (ratioValorActual)
                val ratio = viewModel.ratioValorActual.toDoubleOrNull() ?: 0.0

                val gramos = viewModel.campoGramos.toDoubleOrNull()?.toInt() ?: 0

                if (alimentoHC > 0) {
                    val resultadoDouble = calculoRaciones(alimentoHC, ratio, gramos)

                    val resultado = if (resultadoDouble % 1.0 == 0.0) {
                        resultadoDouble.toInt().toString()
                    } else {
                        resultadoDouble.toString()
                    }
                    textoResultado = "Has de pincharte $resultado UI"
                } else {
                    textoResultado = "No has de pincharte nada"
                }
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(120.dp)
        ) {
            Text("OK")
        }

        Spacer(modifier = Modifier.height(92.dp))
    }
}
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

    // Variables para los campos de texto
    var campo1 by remember { mutableStateOf("") }
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
            value = viewModel.tablaSeleccionada,
            onValueChange = { nuevaTabla ->
                viewModel.onTablaSeleccionada(nuevaTabla)
            },
            label = "Seleccionar Categoría",
            options = viewModel.listaTablas,
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

            /**
            OptimizedDropdown(
                value = campoDropdown2,
                onValueChange = { campoDropdown2 = it },
                label = "Dropdown 2",
                options = opciones,
                modifier = Modifier.weight(1f)
            )
            */
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para la cantidad de gramps
        OutlinedTextField(
            value = campo1,

            // Formato adecuado para dentro de textfield *CAMBIAR SI QUIERES*
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$")))
                    campo1 = it
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
        ResultBox(text = "RESULTADO")

        Spacer(modifier = Modifier.weight(0.8f))

        // Boton para hacer la operacion
        Button(
            onClick = {},
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(120.dp)
        ) {
            Text("OK")
        }

        Spacer(modifier = Modifier.height(92.dp))
    }
}
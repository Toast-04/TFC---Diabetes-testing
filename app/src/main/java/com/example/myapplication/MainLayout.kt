package com.example.myapplication

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Layout principal de la app
@Composable
fun MainLayout(modifier: Modifier = Modifier,
               viewModel: MainViewModel, //<-- Parametro nuevo para el ViewModel
               navController: NavController
) {

    //Resetamos las selecciones al cargar otra pantalla
    LaunchedEffect(Unit) {
        viewModel.resetearSeleccion()
    }

    val focusManager = LocalFocusManager.current

    var textoResultado by remember { mutableStateOf("") }
    //La variables que aqui habian ahora son innecesarias ya que van en el viewmodel

    var avisoError by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()
        //Detector de gestos para quitar el foco del teclado
        .pointerInput(Unit){
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }){

        // Interior de la pantalla principal
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
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

            Spacer(modifier = Modifier.height(50.dp))

            // Caja de resultado
            ResultBox(textoResultado)


            Spacer(modifier = Modifier.height(30.dp))

            // Boton para hacer la operacion
            Button(
                onClick = {
                    // Obtenemos el valor de la ratio como texto desde el ViewModel
                    val ratioTexto = viewModel.ratioValorActual

                    // Validamos si está vacía o es "0" antes de hacer nada
                    if (ratioTexto.isEmpty() || ratioTexto == "0" || ratioTexto == "0.0") {
                        avisoError = true // Esto dispara el AlertDialog
                    } else {
                        // Si existe la ratio, procedemos con el cálculo normal
                        val alimentoHC = viewModel.hcSeleccionado
                        val ratio = ratioTexto.toDoubleOrNull() ?: 0.0
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
                            textoResultado = "Selecciona un alimento"
                        }
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(120.dp)
            ) {
                Text("OK")
            }

            if (avisoError){
                AlertDialog(
                    onDismissRequest = { avisoError = false },
                    title = { Text("Datos incompletos")},
                    text = { Text("Por favor, configura la ratio primero o rellena los campos para obtener un resultado")},
                    confirmButton = {
                        Button (
                            onClick = {
                                avisoError = false
                                navController.navigate("configuracion") {
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Text ("Entendido")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { avisoError = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }


            Spacer(modifier = Modifier.height(80.dp))
        }

        BotonAyuda(infoText = "Guía de uso:\n" +
                "\n" +
                "Seleccionar categoría: Elige el grupo de alimentos que quieres registrar (frutas, verduras, cereales, etc.).\n" +
                "\n" +
                "Seleccionar alimento: Escoge el alimento específico dentro de la categoría seleccionada.\n" +
                "\n" +
                "Cantidad: Indica la cantidad que vas a consumir.\n" +
                "\n" +
                "Momento del día: Selecciona si es desayuno, comida o cena.\n" +
                "\n" +
                "Boton OK: Pulsa el botón para obtener el resultado\n",
                 tamanoIcono = 50.dp,
                 modifier = Modifier
                     .align(Alignment.BottomStart)
                     .padding(start = 20.dp, bottom = 20.dp)
            )
    }
}
package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.util.Calendar

@Composable
fun ConfiguracionScreen(
    viewModel: MainViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    fontSizeOption: String,
    onFontSizeChange: (String) -> Unit,
    modoAccesible: Boolean,
    onModoAccesibleChange: (Boolean) -> Unit
) {

    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            //Detector de gestos para quitar el foco del teclado
            .pointerInput(Unit){
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Título ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Opciones",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- Selector tamaño de letra ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(120.dp), // altura fija
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    "Tamaño de letra",
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val options = listOf("Pequeña", "Normal", "Grande")
                    options.forEach { option ->
                        Button(
                            onClick = { viewModel.updateFontSize(option) },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (fontSizeOption == option)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer
                            )
                        ) {
                            Text(option, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
            }
        }

        // --- Switch modo accesible ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(50.dp), // altura fija
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Modo dislexia", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = viewModel.isDislexiaMode,
                    onCheckedChange = {viewModel.updateModoDislexia(it)}
                )
            }
        }

        // --- Switch modo oscuro ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(50.dp), // altura fija
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Modo Oscuro", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = viewModel.isDarkTheme,
                    onCheckedChange = {viewModel.updateDarkMode(it)}
                )
            }
        }

        // --- Spacer ---
        item { Spacer(modifier = Modifier.height(40.dp)) }

        // Ratio label
        item {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Ratio",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.padding(horizontal = 8.dp))

                BotonAyuda(infoText = "La ratio indica las unidades de insulina por cada ración de HC cubiertos por 1 unidad de insulina, se han de configurar para la mañana, noche y dia, puestos a que esta varía durante el día",
                    tamanoIcono = 24.dp)
            }
        }

        // --- Switch para las ratio ---
        item {
            var isRatioActive by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Ver ratio", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = isRatioActive,
                        onCheckedChange = { isRatioActive = it }
                    )
                }

                if (isRatioActive) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = viewModel.ratioManana,
                            onValueChange = { viewModel.updateRatioManana(it) }, // Llama a la función que guarda
                            label = { Text("Mañana") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = viewModel.ratioMediodia,
                            onValueChange = { viewModel.updateRatioMediodia(it) }, // Llama a la función que guarda
                            label = { Text("Mediodía") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = viewModel.ratioNoche,
                            onValueChange = { viewModel.updateRatioNoche(it) }, // Llama a la función que guarda
                            label = { Text("Noche") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // --- Spacer ---
        item { Spacer(modifier = Modifier.height(40.dp)) }

        // Factor de Sensibilidad label
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Factor de sensibilidad",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.padding(horizontal = 8.dp))

                BotonAyuda(infoText = "El factor de sensibilidad es la cantidad de miligramos por decilitro (mg/dL) que disminuye la glucosa en sangre tras la administración de 1 unidad de insulina rápida, esto es útil para poder saber la cantidad de insulina que se ha de suministrar a la hora de hacer una correción de azúcar",
                    tamanoIcono = 24.dp)
            }

        }
        // --- Switch para las Factor de sensibilidad ---
        item {
            var isFactorActive by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Ver factor de sensibilidad", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = isFactorActive,
                        onCheckedChange = { isFactorActive = it }
                    )
                }

                if (isFactorActive) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = viewModel.factorSensibilidad,
                            onValueChange = {
                                if (it.isEmpty() || it.matches(Regex("^\\d+$"))) {
                                    viewModel.updateFactor(it) // Llama a la función que guarda
                                }
                            },
                            label = { Text("Factor de Sensibilidad") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificacionesScreen(viewModel: MainViewModel, navController: NavController, context: Context) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    val focusManager = LocalFocusManager.current

    // Estado del switch según SharedPreferences
    var isNotificationsActive by remember {
        mutableStateOf(prefs.getBoolean("notifications_enabled", false))
    }

    // Hora seleccionada como string HH:mm
    var selectedTime by remember {
        mutableStateOf(
            String.format(
                "%02d:%02d",
                prefs.getInt("notification_hour", 9),
                prefs.getInt("notification_minute", 0)
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            //Detector de gestos para quitar el foco del teclado
            .pointerInput(Unit){
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Notificaciones",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- Switch para activar notificaciones ---
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Activar Notificaciones", style = MaterialTheme.typography.bodyLarge)

                    Switch(
                        checked = isNotificationsActive,
                        onCheckedChange = { enabled ->
                            isNotificationsActive = enabled

                            // Guardamos el estado en SharedPreferences
                            prefs.edit().putBoolean("notifications_enabled", enabled).apply()

                            // Cancelar notificación si se apaga
                            if (!enabled) cancelNotification(context)
                        }
                    )
                }

                // --- Selección de hora solo si está activado ---
                if (isNotificationsActive) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Recordatorio de insulina lenta:",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(0.4f)
                        )

                        OutlinedTextField(
                            value = selectedTime,
                            onValueChange = { selectedTime = it },
                            label = { Text("Hora (HH:mm)") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(0.5f),
                            trailingIcon = {
                                IconButton(onClick = {
                                    val calendar = Calendar.getInstance()
                                    android.app.TimePickerDialog(
                                        context,
                                        { _, hour: Int, minute: Int ->

                                            selectedTime = String.format("%02d:%02d", hour, minute)

                                            // Guardamos hora y minuto en SharedPreferences
                                            prefs.edit()
                                                .putInt("notification_hour", hour)
                                                .putInt("notification_minute", minute)
                                                .apply()

                                            // Programamos la notificación si el switch está activo
                                            if (isNotificationsActive) {
                                                scheduleNotification(context, hour, minute)
                                            }

                                        },
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE),
                                        true
                                    ).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Seleccionar hora"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModoRecetaScreen(modifier: Modifier = Modifier,
               viewModel: MainViewModel,
                     navController: NavController
) {

    //Resetamos las selecciones al cargar otra pantalla
    LaunchedEffect(Unit) {
        viewModel.resetearSeleccion()
    }

    var textoResultado by remember { mutableStateOf("") }

    //Guardamos aqui el último calculo para poder sumarlo
    var ultimoCalculo by remember { mutableStateOf(0.0) }

    //Variable de estado para mostrar aviso
    var avisoBorrarReceta by remember { mutableStateOf(false) }

    var avisoError by remember { mutableStateOf(false) }


    val focusManager = LocalFocusManager.current


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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "Modo receta",
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

            Spacer(modifier = Modifier.height(32.dp))

            // Caja de resultado
            ResultBox(textoResultado)

            Spacer(modifier = Modifier.height(15.dp))

            //Mostrar el Total acumulado de la receta
            if (viewModel.totalAcumulado > 0) {
                Text(
                    text = "Tu receta completa son : ${viewModel.totalAcumulado} UI",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }


            Spacer(modifier = Modifier.height(30.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                // Boton para hacer la operacion
                Button(
                    onClick = {
                        val ratio = viewModel.ratioValorActual // Obtenemos el valor del string

                        // Verificamos si la ratio para ese momento del día está vacía
                        if (ratio.isEmpty() || ratio == "0") {
                            avisoError = true
                        } else {
                            val alimentoHC = viewModel.hcSeleccionado
                            val ratioNum = ratio.toDoubleOrNull() ?: 0.0
                            val gramos = viewModel.campoGramos.toIntOrNull() ?: 0

                            if (alimentoHC > 0) {
                                val resultadoDouble = calculoRaciones(alimentoHC, ratioNum, gramos)
                                ultimoCalculo = resultadoDouble
                                textoResultado = "Actual: ${if (resultadoDouble % 1.0 == 0.0) resultadoDouble.toInt() else resultadoDouble} UI"
                            } else {
                                textoResultado = "Selecciona un alimento válido"
                            }
                        }
                    },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.size(120.dp)
                ) {
                    Text("OK")
                }

                Spacer(modifier = Modifier.width(16.dp))

                //Boton +
                Button(
                    onClick = {
                        if(ultimoCalculo > 0) {
                            viewModel.añadirAlTotal(ultimoCalculo)
                            ultimoCalculo = 0.0
                            textoResultado = "Añadido"
                        }
                    },
                    enabled = ultimoCalculo > 0,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.size(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("+")
                }
            }

            if (viewModel.totalAcumulado != 0.0){
                TextButton(
                    onClick = {
                        avisoBorrarReceta = true
                    },
                    modifier = Modifier.padding(top = 8.dp),

                    ){
                    Text("Limpiar Receta")
                }
            }

            if (avisoBorrarReceta){
                AlertDialog(
                    onDismissRequest = { avisoBorrarReceta = false },
                    title = { Text("¿Borrar receta?") },
                    text = { Text("Se eliminará todo lo agregado a la receta. ¿Estás seguro?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Aquí ejecutamos la limpieza real
                                viewModel.limpiarTotal()
                                textoResultado = ""
                                ultimoCalculo = 0.0
                                avisoBorrarReceta = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Borrar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { avisoBorrarReceta = false }) {
                            Text("Cancelar")
                        }
                    }
                )
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

            Spacer(modifier = Modifier.height(92.dp))
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
                "Boton OK: Pulsa el boton para obtener el resultado\n" +
                "\n" +
                "Boton +: Pulsa el boton para añadir el resultado a la receta" +
                "\n" +
                "Limpiar Receta: Pulsa el boton para limpiar la receta",
            tamanoIcono = 50.dp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 20.dp)
        )
    }
}

@Composable
fun BajarAzucarScreen(viewModel: MainViewModel,
                      navController: NavController) {

    //Resetamos las selecciones al cargar otra pantalla
    LaunchedEffect(Unit) {
        viewModel.resetearSeleccion()
    }
    //Estados para los inputs de azucar
    var azucarActual by remember { mutableStateOf("") }
    var azucarObjetivo by remember { mutableStateOf("") }

    //Estado para el resultado
    var textoResultado by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    var mostrarAviso by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()
        //Detector de gestos para quitar el foco del teclado
        .pointerInput(Unit){
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        })
    {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "Corrección de glucemia",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            //Input para azucar actual
            OutlinedTextField(
                value = azucarActual,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d+$"))) azucarActual = it
                },
                label = { Text("Azucar actual") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.width(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Input para azucar objetivo
            OutlinedTextField(
                value = azucarObjetivo,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d+$"))) azucarObjetivo = it
                },
                label = { Text("Azucar objetivo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.width(250.dp)
            )

            Spacer(modifier = Modifier.height(72.dp))

            //Pasamos el estado al commponente visual
            ResultBox(textoResultado)

            Spacer(modifier = Modifier.weight(0.8f))

            Button(
                onClick = {
                    //Lógica del botón
                    val sensibilidad = viewModel.factorSensibilidad
                    val objetivo = azucarObjetivo
                    val actual = azucarActual




                    if (sensibilidad.isEmpty() || sensibilidad == "0") {
                        mensajeError = "No has configurado el factor de sensibilidad, por favor configuralo"
                        mostrarAviso = true
                    } else if (objetivo.isEmpty()){
                        mensajeError = "Por favor, introduce primero el azúcar objetivo"
                        mostrarAviso = true
                    }else if (actual.isEmpty()){
                        mensajeError = "Por favor, introduce primero el azúcar actual"
                        mostrarAviso = true
                    } else {
                        val actual = azucarActual.toIntOrNull() ?: 0
                        val objetivo = azucarObjetivo.toIntOrNull() ?: 0
                        val sensibilidadNum = sensibilidad.toIntOrNull() ?: 0

                        val resultadoDouble = calculoAzucar(actual, objetivo, sensibilidadNum)
                        val resultado = if (resultadoDouble % 1.0 == 0.0) resultadoDouble.toInt().toString() else resultadoDouble.toString()
                        textoResultado = "Has de pincharte $resultado UI"
                    }

                },
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(120.dp)
            ) {
                Text("OK")
            }

            Spacer(modifier = Modifier.height(92.dp))
        }

        BotonAyuda(infoText = "Guía de uso:\n" +
                "\n" +
                "Azúcar Actual: Introduce la cantidad de azucar actual en la sangre.\n" +
                "\n" +
                "Azúcar Objetivo: Introduce la cantidad de azucar objetivo en la sangre luego de la corrección.\n" +
                "\n" +
                "Boton OK: Pulsa el boton para obtener el resultado\n",
            tamanoIcono = 50.dp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 20.dp)
        )

        if (mostrarAviso) {
            AlertDialog(
                onDismissRequest = { mostrarAviso = false },
                title = { Text("Datos incompletos") },
                text = { Text(mensajeError) },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarAviso = false
                            // Solo navegamos si el error era por la sensibilidad (ajustes)
                            if (mensajeError.contains("configuralo", ignoreCase = true)) {
                                navController.navigate("configuracion"){
                                    launchSingleTop = true
                                }
                            }else if(mensajeError.contains("objetivo", ignoreCase = true)){
                            }
                        }
                    ) {
                        Text(if (mensajeError.contains("Ajustes")) "Ir a Ajustes" else "Entendido")
                    }
                },
                dismissButton = {
                    if (mensajeError.contains("ajustes")) {
                        TextButton(onClick = { mostrarAviso = false }) {
                            Text("Cancelar")
                        }
                    }
                }
            )
        }
    }
}

fun scheduleNotification(context: Context, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)

        if (timeInMillis <= System.currentTimeMillis()) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) return
    }

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean("notifications_enabled", false)
        val hour = prefs.getInt("notification_hour", 9)
        val minute = prefs.getInt("notification_minute", 0)

        if (!enabled) return

        // Mostramos la notificación
        mostrarNotificacion(
            context,
            "Recordatorio",
            "Son las: " + hour + ":" + minute + " es hora de pincharse la insulina lenta"
        )

        // Reprogramamos para mañana a la misma hora
        scheduleNotification(context, hour, minute)
    }
}


fun cancelNotification(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
    val intent = android.content.Intent(context, NotificationReceiver::class.java)

    val pendingIntent = android.app.PendingIntent.getBroadcast(
        context,
        0,
        intent,
        android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.cancel(pendingIntent)
}

fun isNotificationEnabled(context: Context): Boolean {
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("notifications_enabled", false)
}

fun setNotificationEnabled(context: Context, enabled: Boolean) {
    val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("notifications_enabled", enabled).apply()
}
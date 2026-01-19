package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.round

@Composable
fun ConfiguracionScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    fontSizeOption: String,
    onFontSizeChange: (String) -> Unit,
    modoAccesible: Boolean,
    onModoAccesibleChange: (Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
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
                            onClick = { onFontSizeChange(option) },
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
                    checked = modoAccesible,
                    onCheckedChange = onModoAccesibleChange
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
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange
                )
            }
        }

        // --- Spacer final ---
        item { Spacer(modifier = Modifier.height(40.dp)) }

        // Ratio label
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ratio",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Activar ratio", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = isRatioActive,
                        onCheckedChange = { isRatioActive = it }
                    )
                }

                if (isRatioActive) {
                    Spacer(modifier = Modifier.height(16.dp))

                    var manana by remember { mutableStateOf("") }
                    var mediodia by remember { mutableStateOf("") }
                    var noche by remember { mutableStateOf("") }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = manana,
                            onValueChange = { manana = it },
                            label = { Text("Mañana") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = mediodia,
                            onValueChange = { mediodia = it },
                            label = { Text("Mediodía") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = noche,
                            onValueChange = { noche = it },
                            label = { Text("Noche") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificacionesScreen(navController: NavController, context: Context) {
    val context = LocalContext.current
    var isNotificationsActive by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
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
                    text = "Notificaciones",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- Activar notificaciones ---
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
                        onCheckedChange = { isNotificationsActive = it }
                    )
                }

                // --- Selección de hora solo si está activado ---
                if (isNotificationsActive) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // un pequeño espacio entre label y TextField
                    ) {
                        // Label a la izquierda
                        Text(
                            text = "Recordatorio Lenta:",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(0.4f) // ocupa menos espacio
                        )

                        OutlinedTextField(
                            value = selectedTime,
                            onValueChange = { selectedTime = it },
                            label = { Text("Hora (HH:mm)") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(0.5f),
                            trailingIcon = {
                                IconButton(onClick = {
                                    val calendar = java.util.Calendar.getInstance()
                                    android.app.TimePickerDialog(
                                        context,
                                        { _, hour: Int, minute: Int ->
                                            // Guardamos la hora seleccionada
                                            selectedTime = String.format("%02d:%02d", hour, minute)

                                            // Programamos la notificación solo si el switch está activo
                                            if (isNotificationsActive) {
                                                scheduleNotification(context, hour, minute)
                                            }
                                        },
                                        calendar.get(java.util.Calendar.HOUR_OF_DAY),
                                        calendar.get(java.util.Calendar.MINUTE),
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
fun InformacionScreen(onClose: () -> Unit) {
    // Fondo semitransparente que bloquea interacciones
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                onClick = onClose,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))
                .padding(24.dp)
                .widthIn(max = 300.dp)
                .clickable(
                    onClick = {  },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Información",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Mereketengue.",
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onClose) {
                Text("Cerrar")
            }
        }
    }
}

@Composable
fun ModoRecetaScreen() {
    var campo1 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Modo Receta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = campo1,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) campo1 = it
            },
            label = { Text("Gramos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.width(250.dp)
        )

        Spacer(modifier = Modifier.height(72.dp))

        ResultBox("RESULTADO RECETA")

        Spacer(modifier = Modifier.weight(0.8f))

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

@Composable
fun BajarAzucarScreen() {
    //Estados para los inputs de azucar
    var azucarActual by remember { mutableStateOf("") }
    var azucarObjetivo by remember { mutableStateOf("") }

    //Estado para el resultado
    var textoResultado by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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
                val actual = azucarActual.toIntOrNull() ?: 0
                val objetivo = azucarObjetivo.toIntOrNull() ?: 0

                //Calculamos
                val resultadoDouble = calculoAzucar(actual, objetivo)

                //Lógica visual, si acaba en .0 se muestra numero entero, si acaba .5 se muestra como decimal
                val resultado = if (resultadoDouble % 1.0 == 0.0) {
                    resultadoDouble.toInt().toString()
                } else {
                    resultadoDouble.toString()
                }

                //Actualizamos el estado para que se vea en pantalla
                textoResultado = "Has de pincharte $resultado UI"

            },
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(120.dp)
        ) {
            Text("OK")
        }

        Spacer(modifier = Modifier.height(92.dp))
    }
}

fun scheduleNotification(context: Context, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
    val intent = android.content.Intent(context, NotificationReceiver::class.java)
    val pendingIntent = android.app.PendingIntent.getBroadcast(
        context,
        0,
        intent,
        android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = java.util.Calendar.getInstance().apply {
        set(java.util.Calendar.HOUR_OF_DAY, hour)
        set(java.util.Calendar.MINUTE, minute)
        set(java.util.Calendar.SECOND, 0)
    }

    // Si la hora ya pasó, programa para el día siguiente
    if (calendar.timeInMillis < System.currentTimeMillis()) {
        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
    }

    alarmManager.setExact(
        android.app.AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}

class NotificationReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: android.content.Intent?) {
        context?.let {
            mostrarNotificacion(
                it,
                "Recordatorio",
                "¡Es hora de tu recordatorio!"
            )
        }
    }
}

//Función para la lógica del caluclo
fun calculoAzucar(azucarActual: Int, azucarObjetivo: Int): Double {
    //Calculo de la correción de azucar
    val calculo = ((azucarActual - azucarObjetivo)/50.0)

    //Algoritmo para redondear a 0.5
    return round(calculo * 2) / 2
}
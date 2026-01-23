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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Factor de sensibilidad",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
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
            .padding(horizontal = 20.dp, vertical = 16.dp),
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
                            text = "Recordatorio:",
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
fun BajarAzucarScreen(viewModel: MainViewModel) {
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
                val sensibilidad = viewModel.factorSensibilidad.toIntOrNull() ?: 0


                //Calculamos !!HACER CONTROL POR SI FACTOR DE SENSIBILIDAD ESTA VACIO!!
                val resultadoDouble = calculoAzucar(actual, objetivo, sensibilidad)

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
            "¡Es hora de tu recordatorio!"
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




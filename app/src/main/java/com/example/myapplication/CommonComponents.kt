package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlin.math.round

// Caja que se encarga de mostrar el resultado (llamar a el)
@Composable
fun ResultBox(text: String) {
    // Box para el resultado
    Box(
        modifier = Modifier
            .width(250.dp)
            .height(100.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Texto del resultado
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
// Selectores optimizados
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimizedDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier
) {
    // Estado para controlar el menú desplegable
    var expanded by remember { mutableStateOf(false) }

    // Box para el menú desplegable
    Box(modifier = modifier) {
        // Campo desplegable que detecta si esta abierto o cerrado
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            // Campo de texto con icono de flecha
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                singleLine = true
            )

            // Menú desplegable (acciones)
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onValueChange(opcion)
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
// Boton del menu inferior, para ir a diferentes pantallas
@Composable
fun BotonMenuInferior(navController: NavController) {

    // Estado para controlar el menú desplegable
    var isOpen by remember { mutableStateOf(false) }

    // Botón flotante con icono de flecha
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isOpen) {

            // Boton secundario para el modo bajar azucar
            FloatingActionButton(
                onClick = {
                    navController.navigate("bajar_azucar") {
                        launchSingleTop = true
                    }
                    isOpen = false
                },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            ) {
                // Icono
                Icon(
                    painter = painterResource(id = R.drawable.icono_bajar_azucar),
                    contentDescription = "Bajar azucar",
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Boton secundario para el modo receta
            FloatingActionButton(
                onClick = {
                    navController.navigate("modo_receta") {
                        launchSingleTop = true
                    }
                    isOpen = false
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(56.dp)
            ) {
                // Icono
                Icon(
                    painter = painterResource(id = R.drawable.icono_modo_receta),
                    contentDescription = "Modo receta",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        // Acciones y modificadores del menu *NO TOCAR*
        FloatingActionButton(
            onClick = { isOpen = !isOpen },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        ) {
            // Icono
            Icon(
                painter = painterResource(id = R.drawable.logo_app_better_diabetes),
                contentDescription = "FAB",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun crearCanalNotificacion(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val canal = NotificationChannel(
            "ID_CANAL",
            "Mi Canal",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Canal para notificaciones de prueba" }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(canal)
    }
}

fun mostrarNotificacion(context: Context, titulo: String, mensaje: String) {
    val builder = NotificationCompat.Builder(context, "ID_CANAL")
        .setSmallIcon(R.drawable.logo_app_better_diabetes)
        .setContentTitle(titulo)
        .setContentText(mensaje)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notify(1, builder.build())
    }
}

fun pedirPermisoNotificaciones(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }
}

//Función para la lógica del caluclo
fun calculoAzucar(azucarActual: Int, azucarObjetivo: Int, factorSensibilidad: Int): Double {
    if (factorSensibilidad <= 0) return 0.0 //Evitamos la división por 0

    //Calculo de la correción de azucar
    val calculo = ((azucarActual - azucarObjetivo).toDouble() /factorSensibilidad)

    //Algoritmo para redondear a 0.5
    return round(calculo * 2) / 2
}


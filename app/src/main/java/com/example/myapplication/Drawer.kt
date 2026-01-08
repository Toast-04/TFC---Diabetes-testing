package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Contenido interno dentro de la barra lateral
@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {

    // Contenido interno
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Menú Lateral", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.CenterHorizontally))

        // Divisor
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)

        // Boton interno de la barra lateral: COnfiguraciones
        DrawerButton(
            icon = painterResource(id = R.drawable.icono_configuraciones),
            text = "Configuración",
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("configuracion") {
                    launchSingleTop = true
                }
            }
        )

        // Boton interno de la barra lateral: Notificaciones
        DrawerButton(
            icon = painterResource(id = R.drawable.icono_notificaciones),
            text = "Notificaciones",
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("notificaciones") {
                    launchSingleTop = true
                }
            }
        )
    }
}
// Menu lateral
@Composable
fun DrawerButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit
) {
    // Boton con icono y texto
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, contentDescription = text, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text,
                modifier = Modifier.weight(0.9f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

        }
    }
}
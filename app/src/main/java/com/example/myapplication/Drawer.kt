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

@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Menú Lateral", style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.CenterHorizontally))


        HorizontalDivider(color = Color.Gray, thickness = 1.dp)

        DrawerButton(
            icon = painterResource(id = R.drawable.notificaciones),
            text = "Configuración",
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("configuracion") {
                    launchSingleTop = true
                }
            }
        )

        DrawerButton(
            icon = painterResource(id = R.drawable.notificaciones),
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

@Composable
fun DrawerButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit
) {
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
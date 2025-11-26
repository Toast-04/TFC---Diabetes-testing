package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF87CEEB))
            ) {
                MyApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    // Estado del drawer
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(Color(0xFFADD8E6))
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Text("Menú Lateral", style = MaterialTheme.typography.titleMedium)


                Button(onClick = { /* Acción botón 1 */ }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favorito")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Favorito")
                }

                Button(onClick = { /* Acción botón 2 */ },) {
                    Icon(Icons.Filled.Email, contentDescription = "Correo")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Correo")
                }

                Button(onClick = { /* Acción botón 3 */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartir")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compartir")
                }
            }
        }
    ) {
        // Contenido principal
        Box(modifier = Modifier.fillMaxSize()) {

            // Botón de abrir drawer en esquina superior izquierda
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                    }
                },
                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.TopStart)
                    .size(72.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
            }

            // Tu FAB desplegable horizontal en la esquina inferior derecha
            MyFABMenu()
        }
    }
}

@Composable
fun MyFABMenu() {
    var isOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp, end = 32.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Botones secundarios en horizontal
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 12.dp, end = 90.dp)
        ) {
            if (isOpen) {
                FloatingActionButton(
                    onClick = { /* Acción botón 1 */ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favorito")
                }

                FloatingActionButton(
                    onClick = { /* Acción botón 2 */ },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Calendario")
                }

                FloatingActionButton(
                    onClick = { /* Acción botón 3 */ },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartir")
                }
            }
        }

        // Botón principal
        FloatingActionButton(
            onClick = { isOpen = !isOpen },
            containerColor = Color(0xFF0247E7),
            modifier = Modifier.size(80.dp)
        ) {
        }
    }
}

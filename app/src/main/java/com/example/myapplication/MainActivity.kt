package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.myapplication.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            AppTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { nuevoEstado -> isDarkTheme = nuevoEstado }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Menú Lateral", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Modo Oscuro")
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = onThemeChange
                        )
                    }

                    Button(
                        onClick = { /* Acción botón 1 */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorito")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Favorito")
                    }

                    Button(
                        onClick = { /* Acción botón 2 */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Email, contentDescription = "Correo")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Correo")
                    }

                    Button(
                        onClick = { /* Acción botón 3 */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartir")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Compartir")
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        Surface(
                            modifier = Modifier
                                .width(180.dp)
                                .height(48.dp)
                                .padding(end = 16.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                            color = Color.Transparent
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "logo",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            floatingActionButton = {
                BotonMenuCircular()
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            MainLayoutScreen(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun MainLayoutScreen(modifier: Modifier = Modifier) {
    var campo1 by remember { mutableStateOf("") }
    var campo2 by remember { mutableStateOf("") }
    var campoGrande by remember { mutableStateOf("") }
    var campoAncho by remember { mutableStateOf("") }
    var campoMediano by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = campo1,
                    onValueChange = { campo1 = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Campo 1") },
                    shape = RoundedCornerShape(4.dp)
                )

                OutlinedTextField(
                    value = campo2,
                    onValueChange = { campo2 = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Campo 2") },
                    shape = RoundedCornerShape(4.dp)
                )
            }

            OutlinedTextField(
                value = campoGrande,
                onValueChange = { campoGrande = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 16.dp),
                placeholder = { Text("Campo grande") },
                shape = RoundedCornerShape(4.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = campoAncho,
                onValueChange = { campoAncho = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text("Campo ancho") },
                shape = RoundedCornerShape(4.dp)
            )

            OutlinedTextField(
                value = campoMediano,
                onValueChange = { campoMediano = it },
                modifier = Modifier
                    .width(200.dp)
                    .padding(bottom = 24.dp),
                placeholder = { Text("Campo mediano") },
                shape = RoundedCornerShape(4.dp)
            )
        }
    }
}

@Composable
fun BotonMenuCircular() {
    var isOpen by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        if (isOpen) {
            FloatingActionButton(
                onClick = { /* Acción botón 1 */ },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Info, contentDescription = "Favorito")
            }

            FloatingActionButton(
                onClick = { /* Acción botón 3 */ },
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Share, contentDescription = "Compartir")
            }
        }

        // FAB principal
        FloatingActionButton(
            onClick = { isOpen = !isOpen },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        ) {
            val icon = if (isOpen) Icons.Default.Close else Icons.Default.Add
            Icon(icon, contentDescription = "Abrir o cerrar menú FAB")
        }
    }
}
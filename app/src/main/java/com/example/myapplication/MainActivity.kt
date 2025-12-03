package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
        Scaffold(topBar = { TopAppBar(title = {}, navigationIcon = { IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open()
                                    else drawerState.close()
                                }}
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                        } },
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
                            } } },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayoutScreen(modifier: Modifier = Modifier) {
    // Estado del campo de gramos
    var campo1 by remember { mutableStateOf("") }

    // Dropdown principal (arriba)
    var expandedPrincipal by remember { mutableStateOf(false) }
    var campoDropdownPrincipal by remember { mutableStateOf("") }

    // Dropdowns de la fila
    var expanded1 by remember { mutableStateOf(false) }
    var campoDropdown1 by remember { mutableStateOf("") }
    var expanded2 by remember { mutableStateOf(false) }
    var campoDropdown2 by remember { mutableStateOf("") }

    val opciones = listOf("Opción 1", "Opción 2", "Opción 3")
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .pointerInput(Unit) {
                detectTapGestures { keyboardController?.hide() }
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Dropdown principal (full width)
            ExposedDropdownMenuBox(
                expanded = expandedPrincipal,
                onExpandedChange = { expandedPrincipal = !expandedPrincipal },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = campoDropdownPrincipal,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Dropdown Principal") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedPrincipal) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedPrincipal,
                    onDismissRequest = { expandedPrincipal = false }
                ) {
                    opciones.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                campoDropdownPrincipal = opcion
                                expandedPrincipal = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fila con dos dropdowns centrados y adaptativos
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // separación entre dropdowns
            ) {
                // Dropdown 1
                ExposedDropdownMenuBox(
                    expanded = expanded1,
                    onExpandedChange = { expanded1 = !expanded1 },
                    modifier = Modifier.weight(1f) // ocupa la mitad disponible
                ) {
                    OutlinedTextField(
                        value = campoDropdown1,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Dropdown 1") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded1) },
                        modifier = Modifier.menuAnchor().fillMaxWidth() // asegura que llene el espacio de weight
                    )

                    ExposedDropdownMenu(
                        expanded = expanded1,
                        onDismissRequest = { expanded1 = false }
                    ) {
                        opciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    campoDropdown1 = opcion
                                    expanded1 = false
                                }
                            )
                        }
                    }
                }

                // Dropdown 2
                ExposedDropdownMenuBox(
                    expanded = expanded2,
                    onExpandedChange = { expanded2 = !expanded2 },
                    modifier = Modifier.weight(1f) // ocupa la otra mitad disponible
                ) {
                    OutlinedTextField(
                        value = campoDropdown2,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Dropdown 2") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded2) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded2,
                        onDismissRequest = { expanded2 = false }
                    ) {
                        opciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    campoDropdown2 = opcion
                                    expanded2 = false
                                }
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Campo de gramos centrado
            OutlinedTextField(
                value = campo1,
                onValueChange = { campo1 = it },
                placeholder = { Text("Gramos") },
                label = { Text("Gramos") },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.width(250.dp)
            )

            Spacer(modifier = Modifier.height(72.dp))

            Box(
                modifier = Modifier
                    .width(250.dp)
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RESULTADO",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botón circular grande
            Button(
                onClick = { /* Acción del botón */ },
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(120.dp)
            ) {
                Text("OK")
            }
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
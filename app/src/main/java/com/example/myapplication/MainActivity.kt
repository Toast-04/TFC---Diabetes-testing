package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
                DrawerContent(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    }
                )
            },
            floatingActionButton = {
                BotonMenuCircular()
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            MainLayout(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun DrawerContent(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
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

        DrawerButton(
            icon = Icons.Filled.Favorite,
            text = "Favorito",
            onClick = { /* Acción botón 1 */ }
        )

        DrawerButton(
            icon = Icons.Filled.Email,
            text = "Correo",
            onClick = { /* Acción botón 2 */ }
        )

        DrawerButton(
            icon = Icons.Filled.Share,
            text = "Compartir",
            onClick = { /* Acción botón 3 */ }
        )
    }
}

@Composable
private fun DrawerButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = text)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(modifier: Modifier = Modifier) {
    // Estados estables con keys
    var campo1 by remember { mutableStateOf("") }
    var campoDropdownPrincipal by remember { mutableStateOf("") }
    var campoDropdown1 by remember { mutableStateOf("") }
    var campoDropdown2 by remember { mutableStateOf("") }

    // Lista inmutable y constante
    val opciones = remember { listOf("Opción 1", "Opción 2", "Opción 3") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Dropdown principal optimizado
        OptimizedDropdown(
            value = campoDropdownPrincipal,
            onValueChange = { campoDropdownPrincipal = it },
            label = "Dropdown Principal",
            options = opciones,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fila con dos dropdowns optimizados
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OptimizedDropdown(
                value = campoDropdown1,
                onValueChange = { campoDropdown1 = it },
                label = "Dropdown 1",
                options = opciones,
                modifier = Modifier.weight(1f)
            )

            OptimizedDropdown(
                value = campoDropdown2,
                onValueChange = { campoDropdown2 = it },
                label = "Dropdown 2",
                options = opciones,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TextField optimizado para números
        OutlinedTextField(
            value = campo1,
            onValueChange = { newValue ->
                // Solo permite números y punto decimal
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    campo1 = newValue
                }
            },
            placeholder = { Text("Gramos") },
            label = { Text("Gramos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.width(250.dp)
        )

        Spacer(modifier = Modifier.height(72.dp))

        // Resultado
        ResultBox(text = "RESULTADO")

        Spacer(modifier = Modifier.height(48.dp))

        // Botón OK
        Button(
            onClick = { /* Acción del botón */ },
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(120.dp)
        ) {
            Text("OK")
        }
    }
}

// Dropdown optimizado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OptimizedDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }


    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
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

// Componente separado para el resultado (evita recomposiciones)
@Composable
private fun ResultBox(text: String) {
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
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
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
            SmallFAB(
                icon = Icons.Filled.Info,
                contentDescription = "Info",
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { /* Acción botón 1 */ }
            )

            SmallFAB(
                icon = Icons.Filled.Share,
                contentDescription = "Compartir",
                containerColor = MaterialTheme.colorScheme.tertiary,
                onClick = { /* Acción botón 3 */ }
            )
        }

        // FAB principal
        FloatingActionButton(
            onClick = { isOpen = !isOpen },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                imageVector = if (isOpen) Icons.Default.Close else Icons.Default.Add,
                contentDescription = if (isOpen) "Cerrar" else "Abrir"
            )
        }
    }
}

@Composable
private fun SmallFAB(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = containerColor,
        modifier = Modifier.size(56.dp)
    ) {
        Icon(icon, contentDescription = contentDescription)
    }
}
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.myapplication.ui.theme.AppTheme

// ================================================================
// Main Activity
// ================================================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            // Tema de la app (colores y estilos de letra ("LETRA EN UN FUTURO))
            AppTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false
            ) {
                // Pantalla base
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Ejecutable de la aplicación con opción de modo oscuro
                    MyApp(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { nuevoEstado -> isDarkTheme = nuevoEstado }
                    )
                }
            }
        }
    }
}

// ================================================================
// Composable principal de la app
// ================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {

    // ================================================================
    // Variables necesarias
    // ================================================================
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    var showInfo by remember { mutableStateOf(false) }

    // ================================================================
    // Drawer (Barra lateral)
    // ================================================================
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                DrawerContent(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    navController = navController,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        }
    ) {

        // ================================================================
        // Scaffold principal
        // ================================================================
        Scaffold(
            // ================================================================
            // TopBar condicional
            // ================================================================
            topBar = {
                when (currentRoute) {
                    "main" -> TopBar { scope.launch { drawerState.open() } }
                    "configuracion" -> TopBarOpciones(navController, titulo = "Opciones")
                    "notificaciones" -> TopBarOpciones(navController, titulo = "Notificaciones")
                    else -> {
                        TopBarOpciones(navController, titulo = "")
                    } // Otras pantallas no muestran TopBarOpciones
                }
            },

            containerColor = MaterialTheme.colorScheme.background,

            // ================================================================
            // Botones flotantes solo en main
            // ================================================================
            floatingActionButton = {
                if (currentRoute == "main") {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Botón inferior izquierdo → Overlay de información
                        IconButton(
                            onClick = { showInfo = !showInfo },
                            modifier = Modifier.size(72.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.signo_de_interrogaci_n_icono),
                                contentDescription = "Info",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // Botón inferior derecho → Menú circular
                        BotonMenuInferior(navController)
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,

            // ================================================================
            // Contenido del Scaffold
            // ================================================================
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // ================================================================
                    // Navegación
                    // ================================================================
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("main") { MainLayout() }
                        composable("configuracion") { ConfiguracionScreen(navController = navController, isDarkTheme = isDarkTheme, onThemeChange = onThemeChange) }
                        composable("notificaciones") { NotificiacionesScreen(navController) }
                        composable("informacion") { InformacionScreen {} }
                        composable("modo_receta") { ModoRecetaScreen() }
                        composable("bajar_azucar") { BajarAzucarScreen() }
                    }

                    // ================================================================
                    // Overlay de información
                    // ================================================================
                    if (showInfo) {
                        InformacionScreen(onClose = { showInfo = false })
                    }
                }
            }
        )
    }
}


// ================================================================
// Drawer Content
// ================================================================
@Composable
private fun DrawerContent(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
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
        // Letras menu
        Text("Menú Lateral", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        DrawerButton(
            icon = painterResource(id = R.drawable.notificaciones),
            text = "Configuración",
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("configuracion")
            }
        )

        DrawerButton(
            icon = painterResource(id = R.drawable.notificaciones),
            text = "Notificaciones",
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("notificaciones")
            }
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Modo Oscuro")
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange
                )
            }
        }
    }
}

// ================================================================
// Metodo que define el boton drawer (desplegable lateral)
// ================================================================
@Composable
private fun DrawerButton(
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
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, contentDescription = text, modifier = Modifier.size(24.dp))

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

// ================================================================
// TopBar para todas las pantallas (menos opciones)
// ================================================================
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
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo_app_nombre),
                    contentDescription = "Logo",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .width(160.dp)
                        .height(38.dp)
                )
            }
        }
    )
}

// ================================================================
// Topbar para el menu de opciones
// ================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarOpciones(navController: NavController, titulo: String) {
    TopAppBar(
        title = {
            Text(
                titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver atrás",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
    )
}

// ================================================================
// Layout principal de la app
// ================================================================
@Composable
fun MainLayout(modifier: Modifier = Modifier) {
    var campo1 by remember { mutableStateOf("") }
    var campoDropdownPrincipal by remember { mutableStateOf("") }
    var campoDropdown1 by remember { mutableStateOf("") }
    var campoDropdown2 by remember { mutableStateOf("") }

    val opciones = remember { listOf("Opción 1", "Opción 2", "Opción 3") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            "Pantalla Principal",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OptimizedDropdown(
            value = campoDropdownPrincipal,
            onValueChange = { campoDropdownPrincipal = it },
            label = "Dropdown Principal",
            options = opciones,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        OutlinedTextField(
            value = campo1,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$")))
                    campo1 = it
            },
            placeholder = { Text("Gramos") },
            label = { Text("Gramos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.width(250.dp)
        )

        Spacer(modifier = Modifier.height(72.dp))

        ResultBox(text = "RESULTADO")

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

// ================================================================
// Clase que define los dropDowns
// ================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimizedDropdown(
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
            onExpandedChange = { expanded = it }
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

// ================================================================
// Metodo paralos outputs de los resultados
// ================================================================
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


// Metodo para el boton flotante inferior (Pantalla principal)

@Composable
fun BotonMenuInferior(navController: NavController) {
    var isOpen by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isOpen) {

            FloatingActionButton(
                onClick = { navController.navigate("bajar_azucar") },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icono_bajar_azucar),
                    contentDescription = "Bajar azucar",
                    modifier = Modifier.padding(8.dp)
                )
            }

            FloatingActionButton(
                onClick = { navController.navigate("modo_receta") },
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gorro_cocina_icono),
                    contentDescription = "Modo receta",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

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

// Pantalla modo_receta
@Composable
fun ModoRecetaScreen() {
    var campo1 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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

// Pantalla dentro del drawer (menu lateral) para la configuracion
@Composable
fun ConfiguracionScreen(navController: NavController, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título de la pantalla
        item {
            Text(
                text = "Opciones",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        // Modo Oscuro
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Modo Oscuro", style = MaterialTheme.typography.bodyLarge)
                var isDarkMode by remember { mutableStateOf(false) }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

// Pantalla Notificaciones
@Composable
fun NotificiacionesScreen(navController : NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Pantalla de notificaciones", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Notificaciones", style = MaterialTheme.typography.bodyLarge)
    }
}

// NUEVA INFORMACION SCREEN COMO OVERLAY
@Composable
fun InformacionScreen(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(24.dp)
                .widthIn(max = 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Información",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Mereketengue.")

        }
    }
}

// ================================================================
// Pantalla bajar azucar
// ================================================================
@Composable
fun BajarAzucarScreen() {
    var campo1 by remember { mutableStateOf("") }
    var campo2 by remember { mutableStateOf("") }

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

        OutlinedTextField(
            value = campo1,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d+$"))) campo1 = it
            },
            label = { Text("Azucar actual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.width(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = campo2,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d+$"))) campo2 = it
            },
            label = { Text("Azucar objetivo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.width(250.dp)
        )

        Spacer(modifier = Modifier.height(72.dp))

        ResultBox("RESULTADO")

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

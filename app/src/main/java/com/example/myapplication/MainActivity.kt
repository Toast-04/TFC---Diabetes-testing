package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.myapplication.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope


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
// Clase que ejecuta la aplicacion
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {

    // Variables
    val drawerState = rememberDrawerState(DrawerValue.Closed) // Barra lateral estado
    val scope = rememberCoroutineScope() // Para abrir y cerrar la barra lateral (PRINCIPALMENTE)
    val navController = rememberNavController() // Navcontroller

    val currentBackStackEntry by navController.currentBackStackEntryAsState() // Pila de aplicaciones
    val currentRoute = currentBackStackEntry?.destination?.route // Almacen de la ruta actual

    // Creación del drawer (Barra lateral)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Tamaño del size ajustable (NO TOCAR)
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
        // Estructura principal del proyecto
        Scaffold(
            // TopBar principal (Cabecera)
            topBar = {
                // TopBar según la pantalla
                when (currentRoute) {
                    "configuracion" -> TopBarOpciones(navController, "Configuracion")
                    "notificaciones" -> TopBarOpciones(navController, "Notificaciones")
                    else -> TopBar { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } }
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->

            // BOX SUPERNECESARIO (SI LO QUITAS ROMPES EL CODIGO, DENTRO DE ROW TAMBIEN)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Navegación
                NavHost(
                    navController = navController,
                    startDestination = "main",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    // Rutas del nav controller
                    composable("main") { MainLayout() }
                    composable("configuracion") { ConfiguracionScreen(navController) }
                    composable("notificaciones") { NotificiacionesScreen(navController) }
                    composable("informacion") { InformacionScreen() }
                    composable("modo_receta") { ModoRecetaScreen() }
                    composable("bajar_azucar") { BajarAzucarScreen() }
                }
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)) {
                    // Boton inferior derecho (BotonMenuCircular)
                    if (currentRoute != "configuracion" && currentRoute != "notificaciones") {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                        ) {
                            BotonMenuInferior(navController)
                        }
                    }
                    // Boton inferior izquierdo
                    if (currentRoute != "configuracion" && currentRoute != "notificaciones") {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            BotonInfoInferior(navController)
                        }
                    }
                }
            }
        }
    }
}

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

        // Boton del drawer

        DrawerButton(
            icon = painterResource(id = R.drawable.notificaciones),
            text = "Notificaciones",
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("notificaciones")
            }
        )


        Column (modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Switch modo oscuro en el drawer (menu lateral)
                Text("Modo Oscuro")
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange
                )
            }
        }
    }
}

// Metodo que define el boton drawer (desplegable lateral)
@Composable
private fun DrawerButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit
) {
    // Boton del drawer
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
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )

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
// Topbar para todas las pantallas (menos opciones)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onMenuClick: () -> Unit) {
    // Topbar (Cabecera)
    TopAppBar(
        title = {}, // Se puede cambiar, pero no es necesario
        navigationIcon = {
            // Icono de navegacion pulsable para abrir el drawer
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
            }
        },
        actions = {
            // Cosas para el logo
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
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

// Topbar para el menu de opciones
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

// Layout principal de la app
@Composable
fun MainLayout(modifier: Modifier = Modifier) {
    // Variables para recordad las informaciónes de la pantalla
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
        // DropDownGrande (Este sería para la filtrar las tablas)
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
            // DropDown1 (Para la comida)
            OptimizedDropdown(
                value = campoDropdown1,
                onValueChange = { campoDropdown1 = it },
                label = "Dropdown 1",
                options = opciones,
                modifier = Modifier.weight(1f)
            )

            // DropDown2 (Para la ratio)
            OptimizedDropdown(
                value = campoDropdown2,
                onValueChange = { campoDropdown2 = it },
                label = "Dropdown 2",
                options = opciones,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto (Para los gramos)
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

        // Output
        ResultBox(text = "RESULTADO")

        Spacer(modifier = Modifier.height(48.dp))

        // Boton OK
        Button(
            onClick = {},
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(120.dp)
        ) {
            Text("OK")
        }
    }
}

// Clase que define los dropDowns
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

// Metodo paralos outputs de los resultados
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

            // MiniFAB Info
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

            // MiniFAB modo_receta
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
// Metodo para el boton flotante inferior de la info (Pantalla principal)
@Composable
fun BotonInfoInferior(navController: NavController) {
    IconButton(
        onClick = { navController.navigate("informacion") },
        modifier = Modifier.size(72.dp) // Ajusta el tamaño del área clicable
    ) {
        Icon(
            painter = painterResource(id = R.drawable.signo_de_interrogaci_n_icono),
            contentDescription = "Info",
            modifier = Modifier.size(48.dp), // Tamaño del icono
            tint = MaterialTheme.colorScheme.onBackground // Color del icono
        )
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
        // Texto principal
        Text(
            "Modo Receta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo de texto
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

        // Output modo_receta
        ResultBox("RESULTADO RECETA")

        Spacer(modifier = Modifier.height(48.dp))

        // Boton OK
        Button(
            onClick = {},
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(120.dp)
        ) {
            Text("OK")
        }
    }
}

// Pantalla dentro del drawer (menu lateral) para la configuracion
@Composable
fun ConfiguracionScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Info inside pantalla
        Text("Pantalla de configracion", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Configuración", style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * Codigo provisional
 * Son las pantallas dentro de cada opción individual
 * */

// Pantalla Notificaciones
@Composable
fun NotificiacionesScreen(navController : NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Info inside pantalla
        Text("Pantalla de notificaciones", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Notificaciones", style = MaterialTheme.typography.bodyLarge)
    }
}

// Pantalla informacion
@Composable
fun InformacionScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla Informacion")
    }
}

// Pantalla bajar azucar
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
        // Texto principal
        Text(
            "Corrección de glucemia",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo de texto (Azucar actual)
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

        // Campo de texto 2 (Azucar objetivo)
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

        // Output modo_receta
        ResultBox("RESULTADO")

        Spacer(modifier = Modifier.height(48.dp))

        // Boton OK
        Button(
            onClick = {},
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(120.dp)
        ) {
            Text("OK")
        }
    }
}
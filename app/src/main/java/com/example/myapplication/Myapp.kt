package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.myapplication.ui.theme.AppTheme
import kotlinx.coroutines.launch
// Corazon de la app, lo que se encarga de darle el estilo y tomar acciones
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modoAccesible: Boolean,
    onModoAccesibleChange: (Boolean) -> Unit,
    fontSizeOption: String,
    onFontSizeChange: (String) -> Unit,
    viewModel: MainViewModel //<-- Parametro nuevo para el ViewModel
) {

    // Variables generales
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope() // No tocar
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    var showInfo by remember { mutableStateOf(false) }

    // Cierra el drawer automáticamente al navegar
    LaunchedEffect(currentRoute) {
        if (drawerState.isOpen) drawerState.close()
    }

    // Theme para diseño y estilo
    AppTheme(
        darkTheme = isDarkTheme,
        accesible = modoAccesible,
        fontSizeOption = fontSizeOption
    ) {
        // Drawer y contenidos
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                    DrawerContent(
                        navController = navController,
                        drawerState = drawerState,
                        scope = scope
                    )
                }
            }
        ) {
           // Scaffold principal
            Scaffold(
                // Barra superior y direcciones
                topBar = {
                    when (currentRoute) {
                        "main" -> TopBar { scope.launch { drawerState.open() } }
                        "configuracion" -> TopBarOpciones(navController, titulo = "Opciones")
                        "notificaciones" -> TopBarOpciones(navController, titulo = "Notificaciones")
                        "modo_receta" -> TopBarOpciones(navController, titulo = "Modo Receta")
                        "bajar_azucar" -> TopBarOpciones(navController, titulo = "Corrección")
                    }
                },
                // Acciones de los botones flotantes
                floatingActionButton = {
                    if (currentRoute == "main") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Boton de icono para la informacion
                            IconButton(
                                onClick = { showInfo = !showInfo },
                                modifier = Modifier.size(72.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icono_interrogracion),
                                    contentDescription = "Info",
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            // Boton de menu desplegable
                            BotonMenuInferior(navController)
                        }
                    }
                },
                // Variables permutables del menu flotante
                floatingActionButtonPosition = FabPosition.End,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Navhost controller
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("main") { MainLayout(viewModel = viewModel) }
                        composable("configuracion") {
                            ConfiguracionScreen(
                                isDarkTheme = isDarkTheme,
                                onThemeChange = onThemeChange,
                                fontSizeOption = fontSizeOption,
                                onFontSizeChange = onFontSizeChange,
                                modoAccesible = modoAccesible,
                                onModoAccesibleChange = onModoAccesibleChange
                            )
                        }
                        composable("notificaciones") { NotificiacionesScreen(navController, context = LocalContext.current)}
                        composable("modo_receta") { ModoRecetaScreen() }
                        composable("bajar_azucar") { BajarAzucarScreen() }
                    }

                    // Overlay de informacion
                    if (showInfo) {
                        InformacionScreen(onClose = { showInfo = false })
                    }
                }
            }
        }
    }
}

package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.myapplication.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modoAccesible: Boolean,
    onModoAccesibleChange: (Boolean) -> Unit,
    fontSizeOption: String,
    onFontSizeChange: (String) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    var showInfo by remember { mutableStateOf(false) }

    // Cierra el drawer automáticamente al navegar
    LaunchedEffect(currentRoute) {
        if (drawerState.isOpen) drawerState.close()
    }

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
        AppTheme(
            darkTheme = isDarkTheme,
            accesible = modoAccesible,
            fontSizeOption = fontSizeOption
        ) {
            Scaffold(
                topBar = {
                    when (currentRoute) {
                        "main" -> TopBar { scope.launch { drawerState.open() } }
                        "configuracion" -> TopBarOpciones(navController, titulo = "Opciones")
                        "notificaciones" -> TopBarOpciones(navController, titulo = "Notificaciones")
                        "modo_receta" -> TopBarOpciones(navController, titulo = "Modo Receta")
                        "bajar_azucar" -> TopBarOpciones(navController, titulo = "Corrección")
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                floatingActionButton = {
                    if (currentRoute == "main") {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
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
                            BotonMenuInferior(navController)
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.End
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                ) {
                    // Navegación principal
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("main") { MainLayout() }
                        composable("configuracion") {
                            ConfiguracionScreen(
                                navController = navController,
                                isDarkTheme = isDarkTheme,
                                onThemeChange = onThemeChange,
                                fontSizeOption = fontSizeOption,
                                onFontSizeChange = onFontSizeChange,
                                modoAccesible = modoAccesible,
                                onModoAccesibleChange = onModoAccesibleChange
                            )
                        }
                        composable("notificaciones") { NotificiacionesScreen(navController) }
                        composable("modo_receta") { ModoRecetaScreen() }
                        composable("bajar_azucar") { BajarAzucarScreen() }
                    }

                    // Overlay de información
                    if (showInfo) {
                        InformacionScreen(onClose = { showInfo = false })
                    }
                }
            }
        }
    }
}

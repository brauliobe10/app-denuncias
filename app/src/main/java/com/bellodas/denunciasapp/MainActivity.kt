package com.bellodas.denunciasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bellodas.denunciasapp.ui.screens.BuscarDniScreen
import com.bellodas.denunciasapp.ui.screens.DetalleDenunciaScreen
import com.bellodas.denunciasapp.ui.screens.HomeScreen
import com.bellodas.denunciasapp.ui.screens.ListaDenunciasScreen
import com.bellodas.denunciasapp.ui.screens.RegistrarDenunciaScreen
import com.bellodas.denunciasapp.ui.theme.AzulElectrico
import com.bellodas.denunciasapp.ui.theme.AzulGrisaceoOscuro
import com.bellodas.denunciasapp.ui.theme.AzulInstitucional
import com.bellodas.denunciasapp.ui.theme.Blanco
import com.bellodas.denunciasapp.ui.theme.DenunciasTheme
import com.bellodas.denunciasapp.ui.theme.GrisTextoSecundario
import com.bellodas.denunciasapp.ui.viewmodel.DenunciaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DenunciasTheme {
                DenunciasMainApp()
            }
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Inicio", Icons.Default.Home)
    object Lista : BottomNavItem("lista", "Denuncias", Icons.AutoMirrored.Filled.List)
    object Registrar : BottomNavItem("registrar", "Registrar", Icons.Default.AddCircle)
    object Buscar : BottomNavItem("buscar", "Buscar DNI", Icons.Default.Search)
}

@Composable
fun DenunciasMainApp() {
    val navController = rememberNavController()
    val viewModel: DenunciaViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Lista,
        BottomNavItem.Registrar,
        BottomNavItem.Buscar
    )

    // Ocultar barra inferior cuando se está en la pantalla de detalle
    val showBottomBar = currentRoute in bottomItems.map { it.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Blanco,
                    tonalElevation = 8.dp
                ) {
                    bottomItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = isSelected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AzulElectrico,
                                selectedTextColor = AzulElectrico,
                                indicatorColor = AzulElectrico.copy(alpha = 0.12f),
                                unselectedIconColor = GrisTextoSecundario,
                                unselectedTextColor = GrisTextoSecundario
                            ),
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToList = { navController.navigate(BottomNavItem.Lista.route) },
                    onNavigateToCreate = { navController.navigate(BottomNavItem.Registrar.route) },
                    onNavigateToSearch = { navController.navigate(BottomNavItem.Buscar.route) },
                    onNavigateToDetail = { id -> navController.navigate("detalle/$id") }
                )
            }

            composable(BottomNavItem.Lista.route) {
                ListaDenunciasScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { id -> navController.navigate("detalle/$id") },
                    onNavigateToCreate = { navController.navigate(BottomNavItem.Registrar.route) }
                )
            }

            composable(BottomNavItem.Registrar.route) {
                RegistrarDenunciaScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { id -> navController.navigate("detalle/$id") }
                )
            }

            composable(BottomNavItem.Buscar.route) {
                BuscarDniScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { id -> navController.navigate("detalle/$id") }
                )
            }

            composable(
                route = "detalle/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DetalleDenunciaScreen(
                    denunciaId = id,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

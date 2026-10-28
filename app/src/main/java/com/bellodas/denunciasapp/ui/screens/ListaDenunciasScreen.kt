package com.bellodas.denunciasapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bellodas.denunciasapp.ui.components.DenunciaCard
import com.bellodas.denunciasapp.ui.components.EmptyStateView
import com.bellodas.denunciasapp.ui.components.ErrorView
import com.bellodas.denunciasapp.ui.components.LoadingView
import com.bellodas.denunciasapp.ui.theme.AzulElectrico
import com.bellodas.denunciasapp.ui.theme.AzulGrisaceoOscuro
import com.bellodas.denunciasapp.ui.theme.AzulInstitucional
import com.bellodas.denunciasapp.ui.theme.Blanco
import com.bellodas.denunciasapp.ui.theme.GrisAzuladoClaro
import com.bellodas.denunciasapp.ui.theme.GrisTextoSecundario
import com.bellodas.denunciasapp.ui.theme.NaranjaVibrante
import com.bellodas.denunciasapp.ui.viewmodel.DenunciaViewModel

@Composable
fun ListaDenunciasScreen(
    viewModel: DenunciaViewModel,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.listState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = GrisAzuladoClaro,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = NaranjaVibrante,
                contentColor = Blanco,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nueva Denuncia",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Barra de título de pantalla
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blanco)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            tint = AzulInstitucional,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Lista de Denuncias",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulGrisaceoOscuro
                            )
                            Text(
                                text = "${state.denuncias.size} reportes cargados",
                                fontSize = 12.sp,
                                color = GrisTextoSecundario
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.cargarInicialDenuncias() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refrescar",
                            tint = AzulElectrico
                        )
                    }
                }
            }

            // Manejo de Estados: Carga, Error, Vacío o Lista Paginada
            when {
                state.isLoading -> {
                    LoadingView(
                        message = "Cargando denuncias desde el servidor...",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                state.errorMessage != null && state.denuncias.isEmpty() -> {
                    ErrorView(
                        message = state.errorMessage!!,
                        onRetry = { viewModel.cargarInicialDenuncias() },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                state.denuncias.isEmpty() -> {
                    EmptyStateView(
                        title = "No hay denuncias disponibles",
                        description = "No se encontraron denuncias registradas en la base de datos.",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.denuncias,
                            key = { denuncia -> denuncia.id ?: denuncia.hashCode() }
                        ) { denuncia ->
                            DenunciaCard(
                                denuncia = denuncia,
                                onClick = { denuncia.id?.let { onNavigateToDetail(it) } }
                            )
                        }

                        // Pie de lista con Botón de Paginación para "Cargar los 5 siguientes"
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (state.hasMore) {
                                    Button(
                                        onClick = { viewModel.cargarSiguientesCinco() },
                                        enabled = !state.isLoadingMore,
                                        colors = ButtonDefaults.buttonColors(containerColor = AzulElectrico),
                                        shape = RoundedCornerShape(24.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                    ) {
                                        if (state.isLoadingMore) {
                                            CircularProgressIndicator(
                                                color = Blanco,
                                                strokeWidth = 2.dp,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text("Cargando siguientes 5...", color = Blanco, fontWeight = FontWeight.Bold)
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.ExpandMore,
                                                contentDescription = null,
                                                tint = Blanco
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Cargar los 5 siguientes",
                                                color = Blanco,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "— Has llegado al final de la lista (${state.denuncias.size} denuncias) —",
                                        fontSize = 13.sp,
                                        color = GrisTextoSecundario,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

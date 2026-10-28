package com.bellodas.denunciasapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bellodas.denunciasapp.ui.components.ErrorView
import com.bellodas.denunciasapp.ui.components.LoadingView
import com.bellodas.denunciasapp.ui.components.StatusBadge
import com.bellodas.denunciasapp.ui.theme.AzulElectrico
import com.bellodas.denunciasapp.ui.theme.AzulGrisaceoOscuro
import com.bellodas.denunciasapp.ui.theme.AzulInstitucional
import com.bellodas.denunciasapp.ui.theme.Blanco
import com.bellodas.denunciasapp.ui.theme.GrisAzuladoClaro
import com.bellodas.denunciasapp.ui.theme.GrisTextoSecundario
import com.bellodas.denunciasapp.ui.theme.NaranjaVibrante
import com.bellodas.denunciasapp.ui.theme.RojoError
import com.bellodas.denunciasapp.ui.theme.VerdeAtendido
import com.bellodas.denunciasapp.ui.theme.VerdeFondoAtendido
import com.bellodas.denunciasapp.ui.viewmodel.DenunciaViewModel

@Composable
fun DetalleDenunciaScreen(
    denunciaId: Int,
    viewModel: DenunciaViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.detalleState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(denunciaId) {
        viewModel.cargarDetalle(denunciaId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrisAzuladoClaro)
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Blanco)
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = AzulInstitucional
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Detalle de Denuncia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulGrisaceoOscuro
                    )
                    Text(
                        text = "Código #${denunciaId}",
                        fontSize = 12.sp,
                        color = GrisTextoSecundario
                    )
                }
            }
        }

        when {
            state.isLoading -> {
                LoadingView(
                    message = "Cargando información del reporte...",
                    modifier = Modifier.fillMaxSize()
                )
            }

            state.errorMessage != null && state.denuncia == null -> {
                ErrorView(
                    message = state.errorMessage!!,
                    onRetry = { viewModel.cargarDetalle(denunciaId) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            state.denuncia != null -> {
                val denuncia = state.denuncia!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    // Mensaje de Confirmación tras Atender
                    if (state.successMessage != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = VerdeFondoAtendido),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Atendido",
                                    tint = VerdeAtendido,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = state.successMessage!!,
                                    color = VerdeAtendido,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Tarjeta Principal del Detalle
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Blanco),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(isAtendido = denuncia.isAtendido)

                                Text(
                                    text = "ID: #${denuncia.id ?: "N/A"}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AzulInstitucional
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = denuncia.titulo,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulGrisaceoOscuro
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Fila DNI y Fecha
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = AzulInstitucional,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Ciudadano DNI",
                                            fontSize = 12.sp,
                                            color = GrisTextoSecundario
                                        )
                                    }
                                    Text(
                                        text = denuncia.dni,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AzulGrisaceoOscuro,
                                        modifier = Modifier.padding(start = 24.dp, top = 2.dp)
                                    )
                                }

                                if (!denuncia.fecha.isNullOrEmpty()) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.CalendarToday,
                                                contentDescription = null,
                                                tint = AzulInstitucional,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Fecha Registro",
                                                fontSize = 12.sp,
                                                color = GrisTextoSecundario
                                            )
                                        }
                                        Text(
                                            text = denuncia.fechaFormateada,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = AzulGrisaceoOscuro,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Ubicación
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = NaranjaVibrante,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Ubicación / Dirección:",
                                        fontSize = 12.sp,
                                        color = GrisTextoSecundario
                                    )
                                    Text(
                                        text = denuncia.ubicacion.ifBlank { "No especificada" },
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AzulGrisaceoOscuro
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Descripción
                            Text(
                                text = "Descripción Detallada:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulGrisaceoOscuro
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = GrisAzuladoClaro),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = denuncia.descripcion.ifBlank { "Sin descripción detallada." },
                                    fontSize = 14.sp,
                                    color = AzulGrisaceoOscuro,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            // BOTÓN PRINCIPAL PARA ATENDER DENUNCIA
                            if (denuncia.isAtendido) {
                                Button(
                                    onClick = {},
                                    enabled = false,
                                    colors = ButtonDefaults.buttonColors(disabledContainerColor = VerdeFondoAtendido),
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DoneAll,
                                        contentDescription = "Atendido",
                                        tint = VerdeAtendido,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Denuncia Atendida y Resuelta",
                                        color = VerdeAtendido,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Button(
                                    onClick = {
                                        denuncia.id?.let { viewModel.atenderDenuncia(it) }
                                    },
                                    enabled = !state.isAttending,
                                    colors = ButtonDefaults.buttonColors(containerColor = AzulElectrico),
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                ) {
                                    if (state.isAttending) {
                                        CircularProgressIndicator(
                                            color = Blanco,
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("Procesando atención...", color = Blanco, fontWeight = FontWeight.Bold)
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Atender",
                                            tint = Blanco,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Atender Denuncia",
                                            color = Blanco,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
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
}

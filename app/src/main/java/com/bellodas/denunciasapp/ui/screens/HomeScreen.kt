package com.bellodas.denunciasapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bellodas.denunciasapp.ui.components.DenunciaCard
import com.bellodas.denunciasapp.ui.theme.AzulElectrico
import com.bellodas.denunciasapp.ui.theme.AzulGrisaceoOscuro
import com.bellodas.denunciasapp.ui.theme.AzulInstitucional
import com.bellodas.denunciasapp.ui.theme.Blanco
import com.bellodas.denunciasapp.ui.theme.GrisAzuladoClaro
import com.bellodas.denunciasapp.ui.theme.GrisTextoSecundario
import com.bellodas.denunciasapp.ui.theme.NaranjaPendiente
import com.bellodas.denunciasapp.ui.theme.NaranjaVibrante
import com.bellodas.denunciasapp.ui.theme.VerdeAtendido
import com.bellodas.denunciasapp.ui.viewmodel.DenunciaViewModel

@Composable
fun HomeScreen(
    viewModel: DenunciaViewModel,
    onNavigateToList: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState by viewModel.listState.collectAsState()
    val scrollState = rememberScrollState()

    val total = listState.denuncias.size
    val atendidas = listState.denuncias.count { it.isAtendido }
    val pendientes = total - atendidas

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrisAzuladoClaro)
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        // Encabezado Superior (Header)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Blanco)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(AzulElectrico),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Logo",
                            tint = Blanco,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "DenunciasApp",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulInstitucional
                        )
                        Text(
                            text = "Atención e incidencias ciudadanas",
                            fontSize = 12.sp,
                            color = GrisTextoSecundario
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(GrisAzuladoClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = AzulInstitucional,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {

            // Saludo de bienvenida
            Text(
                text = "¡Hola, Ciudadano!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AzulGrisaceoOscuro
            )
            Text(
                text = "Gestiona y monitorea tus reportes de forma rápida.",
                fontSize = 14.sp,
                color = GrisTextoSecundario
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Tarjetas Estadísticas rápidas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Registradas",
                    count = total.toString(),
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    color = AzulElectrico,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Pendientes",
                    count = pendientes.toString(),
                    icon = Icons.Default.HourglassTop,
                    color = NaranjaPendiente,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Atendidas",
                    count = atendidas.toString(),
                    icon = Icons.Default.CheckCircle,
                    color = VerdeAtendido,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de Acción Principal: Registrar Nueva Denuncia
            ActionCard(
                title = "Registrar Nueva Denuncia",
                subtitle = "Reporta una incidencia o problema rápidamente",
                icon = Icons.Default.ReportProblem,
                backgroundColor = AzulElectrico,
                onClick = onNavigateToCreate
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de Acción Secundaria: Buscar por DNI
            ActionCard(
                title = "Buscar Denuncias por DNI",
                subtitle = "Consulta el estado de reportes por número de DNI",
                icon = Icons.Default.Search,
                backgroundColor = NaranjaVibrante,
                onClick = onNavigateToSearch
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título Sección Denuncias Recientes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Denuncias Recientes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulGrisaceoOscuro
                )
                Text(
                    text = "Ver lista completa",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulElectrico,
                    modifier = Modifier.clickable { onNavigateToList() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Muestra hasta 3 denuncias recientes
            val recientes = listState.denuncias.take(3)
            if (recientes.isEmpty()) {
                Text(
                    text = "No hay denuncias registradas aún.",
                    fontSize = 14.sp,
                    color = GrisTextoSecundario,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                recientes.forEach { denuncia ->
                    DenunciaCard(
                        denuncia = denuncia,
                        onClick = { denuncia.id?.let { onNavigateToDetail(it) } }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    count: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = count,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AzulGrisaceoOscuro
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = GrisTextoSecundario
            )
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blanco
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Blanco.copy(alpha = 0.9f)
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Blanco.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Blanco,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

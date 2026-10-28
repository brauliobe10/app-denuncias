package com.bellodas.denunciasapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bellodas.denunciasapp.data.model.Denuncia
import com.bellodas.denunciasapp.ui.theme.AzulElectrico
import com.bellodas.denunciasapp.ui.theme.AzulGrisaceoOscuro
import com.bellodas.denunciasapp.ui.theme.AzulInstitucional
import com.bellodas.denunciasapp.ui.theme.Blanco
import com.bellodas.denunciasapp.ui.theme.GrisAzuladoClaro
import com.bellodas.denunciasapp.ui.theme.GrisBorde
import com.bellodas.denunciasapp.ui.theme.GrisTextoSecundario
import com.bellodas.denunciasapp.ui.theme.NaranjaFondoPendiente
import com.bellodas.denunciasapp.ui.theme.NaranjaPendiente
import com.bellodas.denunciasapp.ui.theme.NaranjaVibrante
import com.bellodas.denunciasapp.ui.theme.VerdeAtendido
import com.bellodas.denunciasapp.ui.theme.VerdeFondoAtendido

@Composable
fun StatusBadge(
    isAtendido: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isAtendido) VerdeFondoAtendido else NaranjaFondoPendiente
    val textColor = if (isAtendido) VerdeAtendido else NaranjaPendiente
    val icon = if (isAtendido) Icons.Default.CheckCircle else Icons.Default.HourglassTop
    val text = if (isAtendido) "ATENDIDO" else "PENDIENTE"

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DenunciaCard(
    denuncia: Denuncia,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(isAtendido = denuncia.isAtendido)
                if (!denuncia.fecha.isNullOrEmpty()) {
                    Text(
                        text = denuncia.fechaFormateada,
                        fontSize = 12.sp,
                        color = GrisTextoSecundario
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = denuncia.titulo.ifBlank { "Denuncia #${denuncia.id ?: "Sin ID"}" },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AzulGrisaceoOscuro,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (denuncia.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = denuncia.descripcion,
                    fontSize = 13.sp,
                    color = GrisTextoSecundario,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "DNI",
                        tint = AzulInstitucional,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "DNI: ${denuncia.dni}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = AzulGrisaceoOscuro
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ver detalle",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulElectrico
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = AzulElectrico,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingView(
    message: String = "Cargando información...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = AzulElectrico,
            strokeWidth = 3.dp,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            color = AzulGrisaceoOscuro,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Error",
            tint = NaranjaVibrante,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Ocurrió un inconveniente",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AzulGrisaceoOscuro
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            fontSize = 13.sp,
            color = GrisTextoSecundario,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = AzulElectrico),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reintentar",
                tint = Blanco,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Reintentar", color = Blanco, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmptyStateView(
    title: String = "Sin denuncias registradas",
    description: String = "No se encontraron registros para esta consulta.",
    icon: ImageVector = Icons.Default.SearchOff,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(GrisAzuladoClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AzulInstitucional,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AzulGrisaceoOscuro
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            color = GrisTextoSecundario
        )
    }
}

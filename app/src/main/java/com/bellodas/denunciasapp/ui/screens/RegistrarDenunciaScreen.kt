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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bellodas.denunciasapp.ui.theme.AzulElectrico
import com.bellodas.denunciasapp.ui.theme.AzulGrisaceoOscuro
import com.bellodas.denunciasapp.ui.theme.AzulInstitucional
import com.bellodas.denunciasapp.ui.theme.Blanco
import com.bellodas.denunciasapp.ui.theme.GrisAzuladoClaro
import com.bellodas.denunciasapp.ui.theme.GrisBorde
import com.bellodas.denunciasapp.ui.theme.GrisTextoSecundario
import com.bellodas.denunciasapp.ui.theme.RojoError
import com.bellodas.denunciasapp.ui.theme.VerdeAtendido
import com.bellodas.denunciasapp.ui.viewmodel.DenunciaViewModel

@Composable
fun RegistrarDenunciaScreen(
    viewModel: DenunciaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.registrarState.collectAsState()
    val scrollState = rememberScrollState()

    // Dialogo de confirmación cuando se registra la denuncia exitosamente
    if (state.createdDenuncia != null) {
        val creada = state.createdDenuncia!!
        AlertDialog(
            onDismissRequest = {
                viewModel.resetRegistrarState()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Éxito",
                    tint = VerdeAtendido,
                    modifier = Modifier.size(52.dp)
                )
            },
            title = {
                Text(
                    text = "¡Denuncia Registrada!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AzulGrisaceoOscuro
                )
            },
            text = {
                Column {
                    Text(
                        text = "Su denuncia ha sido registrada correctamente con la siguiente referencia:",
                        fontSize = 14.sp,
                        color = GrisTextoSecundario
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GrisAzuladoClaro),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Código de Referencia: #DEN-${creada.id ?: "NEW"}",
                                fontWeight = FontWeight.Bold,
                                color = AzulElectrico,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Asunto: ${creada.titulo}",
                                fontSize = 13.sp,
                                color = AzulGrisaceoOscuro
                            )
                            Text(
                                text = "DNI Ciudadano: ${creada.dni}",
                                fontSize = 13.sp,
                                color = GrisTextoSecundario
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val id = creada.id ?: 1
                        viewModel.resetRegistrarState()
                        onNavigateToDetail(id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulElectrico),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Ver Detalle", color = Blanco, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.resetRegistrarState()
                        onNavigateBack()
                    }
                ) {
                    Text("Volver al Inicio", color = AzulInstitucional)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrisAzuladoClaro)
    ) {
        // Top Header
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
                        contentDescription = "Atrás",
                        tint = AzulInstitucional
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Registrar Denuncia",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulGrisaceoOscuro
                    )
                    Text(
                        text = "Complete el formulario con los datos requeridos",
                        fontSize = 12.sp,
                        color = GrisTextoSecundario
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
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
                    Text(
                        text = "Formulario de Incidencia",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulGrisaceoOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Todos los campos con (*) son obligatorios",
                        fontSize = 12.sp,
                        color = GrisTextoSecundario
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // CAMPO 1: DNI del Ciudadano
                    Text(
                        text = "DNI del Ciudadano *",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulGrisaceoOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.dni,
                        onValueChange = { viewModel.onDniChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. 72849103") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = AzulInstitucional
                            )
                        },
                        isError = state.dniError != null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulElectrico,
                            unfocusedBorderColor = GrisBorde
                        )
                    )
                    if (state.dniError != null) {
                        Text(
                            text = state.dniError!!,
                            color = RojoError,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CAMPO 2: Título o Asunto de la Denuncia
                    Text(
                        text = "Título o Asunto *",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulGrisaceoOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.titulo,
                        onValueChange = { viewModel.onTituloChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. Alumbrado público malogrado") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Title,
                                contentDescription = null,
                                tint = AzulInstitucional
                            )
                        },
                        isError = state.tituloError != null,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulElectrico,
                            unfocusedBorderColor = GrisBorde
                        )
                    )
                    if (state.tituloError != null) {
                        Text(
                            text = state.tituloError!!,
                            color = RojoError,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CAMPO 3: Ubicación o Dirección
                    Text(
                        text = "Ubicación / Dirección *",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulGrisaceoOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.ubicacion,
                        onValueChange = { viewModel.onUbicacionChanged(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. Av. Las Flores 450, San Isidro") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AzulInstitucional
                            )
                        },
                        isError = state.ubicacionError != null,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulElectrico,
                            unfocusedBorderColor = GrisBorde
                        )
                    )
                    if (state.ubicacionError != null) {
                        Text(
                            text = state.ubicacionError!!,
                            color = RojoError,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CAMPO 4: Descripción detallada
                    Text(
                        text = "Descripción de la denuncia *",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulGrisaceoOscuro
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.descripcion,
                        onValueChange = { viewModel.onDescripcionChanged(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        placeholder = { Text("Describa detalladamente el problema o suceso...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = AzulInstitucional
                            )
                        },
                        isError = state.descripcionError != null,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AzulElectrico,
                            unfocusedBorderColor = GrisBorde
                        )
                    )
                    if (state.descripcionError != null) {
                        Text(
                            text = state.descripcionError!!,
                            color = RojoError,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    if (state.errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = state.errorMessage!!,
                            color = RojoError,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Registrar Denuncia
                    Button(
                        onClick = {
                            viewModel.registrarDenuncia {
                                // Exito handled by dialog State
                            }
                        },
                        enabled = !state.isSubmitting,
                        colors = ButtonDefaults.buttonColors(containerColor = AzulElectrico),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        if (state.isSubmitting) {
                            CircularProgressIndicator(
                                color = Blanco,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Enviando denuncia...", color = Blanco, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Enviar",
                                tint = Blanco,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Registrar Denuncia",
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

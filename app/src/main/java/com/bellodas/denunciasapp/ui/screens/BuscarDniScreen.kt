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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
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
import com.bellodas.denunciasapp.ui.components.DenunciaCard
import com.bellodas.denunciasapp.ui.components.EmptyStateView
import com.bellodas.denunciasapp.ui.components.LoadingView
import com.bellodas.denunciasapp.ui.theme.AzulGrisaceoOscuro
import com.bellodas.denunciasapp.ui.theme.AzulInstitucional
import com.bellodas.denunciasapp.ui.theme.Blanco
import com.bellodas.denunciasapp.ui.theme.GrisAzuladoClaro
import com.bellodas.denunciasapp.ui.theme.GrisBorde
import com.bellodas.denunciasapp.ui.theme.GrisTextoSecundario
import com.bellodas.denunciasapp.ui.theme.NaranjaVibrante
import com.bellodas.denunciasapp.ui.theme.RojoError
import com.bellodas.denunciasapp.ui.viewmodel.DenunciaViewModel

@Composable
fun BuscarDniScreen(
    viewModel: DenunciaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.buscarState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GrisAzuladoClaro)
    ) {
        // Header
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
                        text = "Búsqueda por DNI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulGrisaceoOscuro
                    )
                    Text(
                        text = "Consulte denuncias registradas por número de documento",
                        fontSize = 12.sp,
                        color = GrisTextoSecundario
                    )
                }
            }
        }

        // Search Input Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Ingrese el DNI del Ciudadano",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulGrisaceoOscuro
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.queryDni,
                        onValueChange = { viewModel.onQueryDniChanged(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Ej. 72849103") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.PersonSearch,
                                contentDescription = null,
                                tint = AzulInstitucional
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = { viewModel.buscarPorDni() }
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NaranjaVibrante,
                            unfocusedBorderColor = GrisBorde
                        )
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { viewModel.buscarPorDni() },
                        enabled = !state.isSearching,
                        colors = ButtonDefaults.buttonColors(containerColor = NaranjaVibrante),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.height(54.dp)
                    ) {
                        if (state.isSearching) {
                            CircularProgressIndicator(
                                color = Blanco,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Blanco,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Buscar", color = Blanco, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = state.errorMessage!!,
                        color = RojoError,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        // Result List or Empty States
        when {
            state.isSearching -> {
                LoadingView(
                    message = "Buscando denuncias asociadas al DNI ${state.queryDni}...",
                    modifier = Modifier.fillMaxSize()
                )
            }

            !state.hasSearched -> {
                EmptyStateView(
                    title = "Inicia la búsqueda por DNI",
                    description = "Escribe un número de DNI de 8 dígitos y presiona 'Buscar' para consultar los reportes.",
                    icon = Icons.Default.PersonSearch,
                    modifier = Modifier.fillMaxSize()
                )
            }

            state.resultados.isEmpty() -> {
                EmptyStateView(
                    title = "Sin resultados para el DNI ${state.queryDni}",
                    description = "No se encontraron denuncias asociadas a este número de documento.",
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Se encontraron ${state.resultados.size} denuncia(s) para el DNI ${state.queryDni}:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulGrisaceoOscuro,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.resultados,
                            key = { denuncia -> denuncia.id ?: denuncia.hashCode() }
                        ) { denuncia ->
                            DenunciaCard(
                                denuncia = denuncia,
                                onClick = { denuncia.id?.let { onNavigateToDetail(it) } }
                            )
                        }
                    }
                }
            }
        }
    }
}

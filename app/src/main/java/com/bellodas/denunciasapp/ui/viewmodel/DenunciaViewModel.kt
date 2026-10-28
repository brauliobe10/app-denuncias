package com.bellodas.denunciasapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bellodas.denunciasapp.data.model.Denuncia
import com.bellodas.denunciasapp.data.model.DenunciaRequest
import com.bellodas.denunciasapp.data.repository.DenunciaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UI State para la lista paginada de denuncias
data class DenunciaListUiState(
    val denuncias: List<Denuncia> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true
)

// UI State para el formulario de registro
data class RegistrarUiState(
    val dni: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val ubicacion: String = "",
    val dniError: String? = null,
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val ubicacionError: String? = null,
    val isSubmitting: Boolean = false,
    val createdDenuncia: Denuncia? = null,
    val errorMessage: String? = null
)

// UI State para el detalle de la denuncia
data class DetalleUiState(
    val denuncia: Denuncia? = null,
    val isLoading: Boolean = false,
    val isAttending: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

// UI State para la búsqueda por DNI
data class BuscarDniUiState(
    val queryDni: String = "",
    val resultados: List<Denuncia> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false,
    val errorMessage: String? = null
)

class DenunciaViewModel(
    private val repository: DenunciaRepository = DenunciaRepository()
) : ViewModel() {

    // States
    private val _listState = MutableStateFlow(DenunciaListUiState())
    val listState: StateFlow<DenunciaListUiState> = _listState.asStateFlow()

    private val _registrarState = MutableStateFlow(RegistrarUiState())
    val registrarState: StateFlow<RegistrarUiState> = _registrarState.asStateFlow()

    private val _detalleState = MutableStateFlow(DetalleUiState())
    val detalleState: StateFlow<DetalleUiState> = _detalleState.asStateFlow()

    private val _buscarState = MutableStateFlow(BuscarDniUiState())
    val buscarState: StateFlow<BuscarDniUiState> = _buscarState.asStateFlow()

    init {
        cargarInicialDenuncias()
    }

    // --- LISTA PAGINADA (5 EN 5) ---
    fun cargarInicialDenuncias() {
        viewModelScope.launch {
            _listState.update { it.copy(isLoading = true, errorMessage = null, currentPage = 0) }
            val result = repository.getDenuncias(page = 0, pageSize = 5)
            result.onSuccess { list ->
                _listState.update {
                    it.copy(
                        denuncias = list,
                        isLoading = false,
                        currentPage = 0,
                        hasMore = list.size >= 5
                    )
                }
            }.onFailure { err ->
                _listState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = err.localizedMessage ?: "Error al conectar con el servidor"
                    )
                }
            }
        }
    }

    fun cargarSiguientesCinco() {
        if (_listState.value.isLoadingMore || !_listState.value.hasMore) return

        viewModelScope.launch {
            val nextPage = _listState.value.currentPage + 1
            _listState.update { it.copy(isLoadingMore = true) }

            val result = repository.getDenuncias(page = nextPage, pageSize = 5)
            result.onSuccess { newList ->
                _listState.update { current ->
                    val combined = current.denuncias + newList
                    current.copy(
                        denuncias = combined,
                        isLoadingMore = false,
                        currentPage = nextPage,
                        hasMore = newList.size >= 5
                    )
                }
            }.onFailure { err ->
                _listState.update {
                    it.copy(
                        isLoadingMore = false,
                        errorMessage = err.localizedMessage ?: "Error al cargar más denuncias"
                    )
                }
            }
        }
    }

    // --- DETALLE Y ATENDER DENUNCIA ---
    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _detalleState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            
            // Primero buscar en lista en memoria para respuesta instantánea
            val existing = _listState.value.denuncias.find { it.id == id }
            if (existing != null) {
                _detalleState.update { it.copy(denuncia = existing, isLoading = false) }
            }

            val result = repository.getDenunciaById(id)
            result.onSuccess { denuncia ->
                _detalleState.update { it.copy(denuncia = denuncia, isLoading = false) }
            }.onFailure { err ->
                if (_detalleState.value.denuncia == null) {
                    _detalleState.update {
                        it.copy(isLoading = false, errorMessage = err.localizedMessage ?: "No se pudo obtener el detalle")
                    }
                }
            }
        }
    }

    fun atenderDenuncia(id: Int) {
        viewModelScope.launch {
            _detalleState.update { it.copy(isAttending = true, errorMessage = null, successMessage = null) }
            val result = repository.atenderDenuncia(id)
            result.onSuccess { actualizada ->
                _detalleState.update {
                    it.copy(
                        denuncia = actualizada,
                        isAttending = false,
                        successMessage = "¡Denuncia #${actualizada.id ?: id} marcada como ATENDIDA exitosamente!"
                    )
                }
                // Actualizar en la lista principal
                _listState.update { current ->
                    val updatedList = current.denuncias.map {
                        if (it.id == actualizada.id) actualizada else it
                    }
                    current.copy(denuncias = updatedList)
                }
            }.onFailure { err ->
                _detalleState.update {
                    it.copy(
                        isAttending = false,
                        errorMessage = err.localizedMessage ?: "Error al atender la denuncia"
                    )
                }
            }
        }
    }

    fun clearDetalleMessages() {
        _detalleState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    // --- FORMULARIO DE REGISTRO CON VALIDACIONES ---
    fun onDniChanged(newDni: String) {
        if (newDni.length <= 8 && newDni.all { it.isDigit() }) {
            _registrarState.update { it.copy(dni = newDni, dniError = null) }
        }
    }

    fun onTituloChanged(newTitulo: String) {
        _registrarState.update { it.copy(titulo = newTitulo, tituloError = null) }
    }

    fun onDescripcionChanged(newDesc: String) {
        _registrarState.update { it.copy(descripcion = newDesc, descripcionError = null) }
    }

    fun onUbicacionChanged(newUbi: String) {
        _registrarState.update { it.copy(ubicacion = newUbi, ubicacionError = null) }
    }

    fun registrarDenuncia(onSuccessCallback: (Denuncia) -> Unit) {
        val state = _registrarState.value

        var isValid = true
        var dniErr: String? = null
        var tituloErr: String? = null
        var descErr: String? = null
        var ubiErr: String? = null

        if (state.dni.isBlank()) {
            dniErr = "El DNI es obligatorio"
            isValid = false
        } else if (state.dni.length != 8) {
            dniErr = "El DNI debe tener exactamente 8 dígitos"
            isValid = false
        }

        if (state.titulo.isBlank()) {
            tituloErr = "El título o asunto es obligatorio"
            isValid = false
        } else if (state.titulo.trim().length < 4) {
            tituloErr = "El título debe tener al menos 4 caracteres"
            isValid = false
        }

        if (state.ubicacion.isBlank()) {
            ubiErr = "La ubicación o dirección es obligatoria"
            isValid = false
        }

        if (state.descripcion.isBlank()) {
            descErr = "La descripción de la denuncia es obligatoria"
            isValid = false
        } else if (state.descripcion.trim().length < 10) {
            descErr = "Proporcione una descripción detallada (mín. 10 caracteres)"
            isValid = false
        }

        if (!isValid) {
            _registrarState.update {
                it.copy(
                    dniError = dniErr,
                    tituloError = tituloErr,
                    descripcionError = descErr,
                    ubicacionError = ubiErr
                )
            }
            return
        }

        viewModelScope.launch {
            _registrarState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val currentFecha = java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault()
            ).format(java.util.Date())

            val request = DenunciaRequest(
                dni = state.dni.trim(),
                tipo = state.titulo.trim(),
                titulo = state.titulo.trim(),
                direccion = state.ubicacion.trim(),
                ubicacion = state.ubicacion.trim(),
                descripcion = state.descripcion.trim(),
                fecha = currentFecha,
                estado = 1
            )

            val result = repository.registrarDenuncia(request)
            result.onSuccess { creada ->
                _registrarState.update {
                    RegistrarUiState(
                        createdDenuncia = creada
                    )
                }
                // Refrescar lista para ver la nueva denuncia
                cargarInicialDenuncias()
                onSuccessCallback(creada)
            }.onFailure { err ->
                _registrarState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = err.localizedMessage ?: "Error al registrar la denuncia"
                    )
                }
            }
        }
    }

    fun resetRegistrarState() {
        _registrarState.value = RegistrarUiState()
    }

    // --- BÚSQUEDA POR DNI ---
    fun onQueryDniChanged(newDni: String) {
        if (newDni.length <= 8 && newDni.all { it.isDigit() }) {
            _buscarState.update { it.copy(queryDni = newDni, errorMessage = null) }
        }
    }

    fun buscarPorDni() {
        val query = _buscarState.value.queryDni.trim()
        if (query.length != 8) {
            _buscarState.update {
                it.copy(errorMessage = "Ingrese un DNI válido de 8 dígitos")
            }
            return
        }

        viewModelScope.launch {
            _buscarState.update { it.copy(isSearching = true, errorMessage = null, hasSearched = true) }
            val result = repository.buscarPorDni(query)
            result.onSuccess { resultados ->
                _buscarState.update {
                    it.copy(
                        resultados = resultados,
                        isSearching = false
                    )
                }
            }.onFailure { err ->
                _buscarState.update {
                    it.copy(
                        isSearching = false,
                        errorMessage = err.localizedMessage ?: "Error al realizar la búsqueda"
                    )
                }
            }
        }
    }
}

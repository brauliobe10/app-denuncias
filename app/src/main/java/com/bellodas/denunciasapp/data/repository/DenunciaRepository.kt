package com.bellodas.denunciasapp.data.repository

import com.bellodas.denunciasapp.data.model.Denuncia
import com.bellodas.denunciasapp.data.model.DenunciaRequest
import com.bellodas.denunciasapp.data.network.ApiService
import com.bellodas.denunciasapp.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DenunciaRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    private fun parseErrorMessage(response: retrofit2.Response<*>): String {
        return try {
            val errorJson = response.errorBody()?.string()
            if (!errorJson.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorJson)
                if (jsonObject.has("message")) {
                    return jsonObject.getString("message")
                }
            }
            "Error (${response.code()}): ${response.message()}"
        } catch (_: Exception) {
            "Error (${response.code()}): ${response.message()}"
        }
    }

    // Obtener lista paginada de denuncias desde la API (GET /api/denuncias?offset=X&limit=5)
    suspend fun getDenuncias(page: Int, pageSize: Int = 5): Result<List<Denuncia>> = withContext(Dispatchers.IO) {
        try {
            val offset = page * pageSize
            val response = apiService.getDenuncias(offset = offset, limit = pageSize)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(parseErrorMessage(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Registrar nueva denuncia (POST /api/denuncias)
    suspend fun registrarDenuncia(request: DenunciaRequest): Result<Denuncia> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.registrarDenuncia(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(parseErrorMessage(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener detalle por ID (GET /api/denuncias/{id})
    suspend fun getDenunciaById(id: Int): Result<Denuncia> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getDenunciaById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(parseErrorMessage(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Atender denuncia cambiando estado de 1 a 0 (PUT /api/denuncias/{id})
    suspend fun atenderDenuncia(id: Int): Result<Denuncia> = withContext(Dispatchers.IO) {
        try {
            var response = apiService.atenderDenuncia(id, mapOf("estado" to 0))

            if (!response.isSuccessful) {
                val original = getDenunciaById(id).getOrNull()
                if (original != null) {
                    val actualizada = original.copy(estado = 0)
                    response = apiService.atenderDenunciaObject(id, actualizada)
                }
            }

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else if (response.isSuccessful) {
                val original = getDenunciaById(id).getOrNull()
                val atendida = (original ?: Denuncia(id = id)).copy(estado = 0)
                Result.success(atendida)
            } else {
                Result.failure(Exception(parseErrorMessage(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Buscar denuncias por DNI (GET /api/denuncias/ciudadano/{dni})
    suspend fun buscarPorDni(dni: String): Result<List<Denuncia>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getDenunciasByDni(dni.trim())
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(parseErrorMessage(response)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

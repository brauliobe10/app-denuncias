package com.bellodas.denunciasapp.data.network

import com.bellodas.denunciasapp.data.model.Denuncia
import com.bellodas.denunciasapp.data.model.DenunciaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/denuncias")
    suspend fun registrarDenuncia(
        @Body request: DenunciaRequest
    ): Response<Denuncia>

    @GET("api/denuncias")
    suspend fun getDenuncias(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 5
    ): Response<List<Denuncia>>

    @GET("api/denuncias/{id}")
    suspend fun getDenunciaById(
        @Path("id") id: Int
    ): Response<Denuncia>

    @GET("api/denuncias/ciudadano/{dni}")
    suspend fun getDenunciasByDni(
        @Path("dni") dni: String
    ): Response<List<Denuncia>>

    @PUT("api/denuncias/{id}")
    suspend fun atenderDenuncia(
        @Path("id") id: Int,
        @Body body: Map<String, Int> = mapOf("estado" to 0)
    ): Response<Denuncia>

    @PUT("api/denuncias/{id}")
    suspend fun atenderDenunciaObject(
        @Path("id") id: Int,
        @Body denuncia: Denuncia
    ): Response<Denuncia>
}

package com.bellodas.denunciasapp.data.model

import com.google.gson.annotations.SerializedName

data class Denuncia(
    @SerializedName("id", alternate = ["idDenuncia", "codigo", "_id"])
    val id: Int? = null,

    @SerializedName("dni", alternate = ["dniCiudadano", "dniUsuario", "documento"])
    val dni: String = "",

    @SerializedName("tipo", alternate = ["titulo", "title", "delito", "asunto", "categoria"])
    val tipo: String = "",

    @SerializedName("direccion", alternate = ["ubicacion", "lugar", "address"])
    val direccion: String = "",

    @SerializedName("descripcion", alternate = ["description", "detalle", "observacion"])
    val descripcion: String = "",

    @SerializedName("fecha", alternate = ["fechaRegistro", "createdAt", "fechaHora", "date"])
    val fecha: String? = null,

    @SerializedName("estado", alternate = ["status", "estadoDenuncia"])
    val estado: Any? = 1
) {
    val titulo: String
        get() = tipo.ifBlank { "Denuncia #${id ?: ""}" }

    val ubicacion: String
        get() = direccion.ifBlank { "Ubicación no especificada" }

    val isAtendido: Boolean
        get() {
            return when (estado) {
                0, 0.0, "0", "0.0" -> true
                "ATENDIDO", "PROCESADO", "RESUELTO" -> true
                false -> true
                else -> false
            }
        }

    val estadoTexto: String
        get() = if (isAtendido) "ATENDIDO" else "PENDIENTE"

    // Formatea la fecha de la API al formato local: dd/MM/yyyy HH:mm:ss (fecha, hora, minutos y segundos)
    val fechaFormateada: String
        get() {
            val raw = fecha?.trim()
            if (raw.isNullOrEmpty()) return "Sin fecha"

            return try {
                val cleaned = raw.replace("T", " ").replace("Z", "")
                val mainPart = cleaned.split(".")[0]

                var parsedDate: java.util.Date? = null
                val formats = listOf(
                    java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()),
                    java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                )

                for (fmt in formats) {
                    try {
                        parsedDate = fmt.parse(mainPart)
                        if (parsedDate != null) break
                    } catch (_: Exception) {}
                }

                if (parsedDate != null) {
                    val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault())
                    outputFormat.format(parsedDate)
                } else {
                    raw
                }
            } catch (_: Exception) {
                raw
            }
        }
}

data class DenunciaRequest(
    @SerializedName("dni")
    val dni: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("ubicacion")
    val ubicacion: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("estado")
    val estado: Int = 1
)

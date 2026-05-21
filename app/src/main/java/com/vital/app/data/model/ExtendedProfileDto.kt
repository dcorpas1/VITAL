package com.vital.app.data.model

data class ExtendedProfileDto(
    val uid: String = "",
    val diasEntrenamiento: Int = 3,
    val estiloEntrenamiento: String = "hipertrofia",
    val musculosPrioritarios: List<String> = emptyList(),
    val horasSueno: Int = 7,
    val nivelEstres: String = "medio",
    val alergias: List<String> = emptyList(),
    val intolerancias: List<String> = emptyList(),
    val pesoObjetivo: Double = 0.0,
    val tiempoObjetivo: Int = 3,
    val perfilExtendidoCompletado: Boolean = false
)
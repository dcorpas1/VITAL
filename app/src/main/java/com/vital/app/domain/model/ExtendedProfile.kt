package com.vital.app.domain.model

data class ExtendedProfile(
    val uid: String = "",
    val diasEntrenamiento: Int = 3,        // días por semana
    val estiloEntrenamiento: String = "hipertrofia", // "fuerza" | "hipertrofia" | "resistencia"
    val musculosPrioritarios: List<String> = emptyList(),
    val horasSueno: Int = 7,
    val nivelEstres: String = "medio",     // "bajo" | "medio" | "alto"
    val alergias: List<String> = emptyList(),
    val intolerancias: List<String> = emptyList(),
    val pesoObjetivo: Double = 0.0,
    val tiempoObjetivo: Int = 3            // meses
)
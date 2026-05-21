package com.vital.app.domain.model

data class UserProfile(
    val uid: String = "",
    val nombre: String = "",
    val edad: Int = 0,
    val peso: Double = 0.0,
    val altura: Double = 0.0,
    val sexo: String = "hombre",       // "hombre" | "mujer"
    val objetivo: String = "",          // "perder peso" | "mantener peso" | "ganar músculo"
    val actividad: String = "sedentario",
    val alergias: List<String> = emptyList(),
    val material: String = "gimnasio", // "gimnasio" | "casa"
    val onboardingCompletado: Boolean = false
)
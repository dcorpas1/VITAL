package com.vital.app.data.model

data class UserProfileDto(
    val uid: String = "",
    val nombre: String = "",
    val edad: Int = 0,
    val peso: Double = 0.0,
    val altura: Double = 0.0,
    val sexo: String = "hombre",
    val objetivo: String = "",
    val actividad: String = "sedentario",
    val alergias: List<String> = emptyList(),
    val material: String = "gimnasio",
    val onboardingCompletado: Boolean = false,
    // Resultados del Motor VITAL — también se persisten
    val tmb: Double = 0.0,
    val tdee: Double = 0.0,
    val caloriasObjetivo: Double = 0.0,
    val proteinas: Double = 0.0,
    val grasas: Double = 0.0,
    val carbohidratos: Double = 0.0
)
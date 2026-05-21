package com.vital.app.domain.model

data class NutritionResult(
    val tmb: Double,
    val tdee: Double,
    val caloriasObjetivo: Double,
    val proteinas: Double,
    val grasas: Double,
    val carbohidratos: Double
)
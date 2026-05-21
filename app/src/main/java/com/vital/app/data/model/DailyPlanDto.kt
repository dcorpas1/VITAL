package com.vital.app.data.model

data class DailyPlanDto(
    val fecha: String = "",
    val tipoEntrenamiento: String = "",
    val ejercicios: List<ExerciseDto> = emptyList(),
    val desayuno: FoodDto = FoodDto(),
    val comida: FoodDto = FoodDto(),
    val cena: FoodDto = FoodDto(),
    val habito: String = "",
    val completado: Boolean = false
)
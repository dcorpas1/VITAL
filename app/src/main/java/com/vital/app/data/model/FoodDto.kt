package com.vital.app.data.model

data class FoodDto(
    val id: String = "",
    val nombre: String = "",
    val calorias: Int = 0,
    val proteinas: Double = 0.0,
    val carbohidratos: Double = 0.0,
    val grasas: Double = 0.0,
    val tipoComida: String = "",     // "desayuno", "comida", "cena", "snack"
    val alergenos: List<String> = emptyList()
)
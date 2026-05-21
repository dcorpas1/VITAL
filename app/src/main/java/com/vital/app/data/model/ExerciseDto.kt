package com.vital.app.data.model

data class ExerciseDto(
    val id: String = "",
    val nombre: String = "",
    val grupoMuscular: String = "",  // "pecho", "espalda", "pierna", "hombro", "biceps", "triceps"
    val tipo: String = "",           // "Push", "Pull", "Legs", "Torso", "Pierna"
    val series: Int = 3,
    val repeticiones: String = "8-12",
    val descanso: String = "60s",
    val material: String = "gimnasio" // "gimnasio" | "casa"
)
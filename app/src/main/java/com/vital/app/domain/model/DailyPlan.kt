package com.vital.app.domain.model

data class DailyPlan(
    val fecha: String = "",             // formato "2026-05-20"
    val tipoEntrenamiento: String = "", // "Push" | "Pull" | "Legs" | "Torso" | "Pierna" | "Descanso"
    val comidas: List<String> = emptyList(),
    val ejercicios: List<String> = emptyList(),
    val habito: String = "",
    val completado: Boolean = false
)
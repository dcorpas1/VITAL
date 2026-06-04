package com.vital.app.domain.usecase

import com.vital.app.data.model.DailyPlanDto
import com.vital.app.data.model.FoodDto
import com.vital.app.data.repository.CatalogRepository
import com.vital.app.domain.model.UserProfile
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Constructor sin valor por defecto: Hilt inyecta CatalogRepository automáticamente
class GeneratePlanUseCase(
    private val catalogRepository: CatalogRepository
) {

    private val splitGimnasio = listOf("Push", "Pull", "Legs", "Push", "Pull", "Legs", "Descanso")
    private val splitCasa = listOf("Torso", "Pierna", "Torso", "Pierna", "Torso", "Pierna", "Descanso")

    private val habitos = listOf(
        "Bebe 2.5L de agua hoy",
        "10 minutos de meditación",
        "Duerme 8 horas esta noche",
        "Sal a caminar 20 minutos",
        "Estira 10 minutos antes de dormir",
        "Sin pantallas 1h antes de dormir",
        "Prepara tu comida del día siguiente"
    )

    suspend fun execute(profile: UserProfile): List<DailyPlanDto> {
        val ejercicios = catalogRepository.getEjercicios(profile.material)
        val desayunos = catalogRepository.getAlimentos("desayuno", profile.alergias)
        val comidas = catalogRepository.getAlimentos("comida", profile.alergias)
        val cenas = catalogRepository.getAlimentos("cena", profile.alergias)

        val split = if (profile.material == "gimnasio") splitGimnasio else splitCasa
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val hoy = LocalDate.now()

        return (0 until 30).map { diaIndex ->
            val fecha = hoy.plusDays(diaIndex.toLong()).format(formatter)
            val tipoEntrenamiento = split[diaIndex % 7]

            val ejerciciosDelDia = if (tipoEntrenamiento == "Descanso") {
                emptyList()
            } else {
                ejercicios
                    .filter { it.tipo == tipoEntrenamiento }
                    .shuffled()
                    .take(4)
            }

            DailyPlanDto(
                fecha = fecha,
                tipoEntrenamiento = tipoEntrenamiento,
                ejercicios = ejerciciosDelDia,
                desayuno = desayunos.randomOrNull() ?: FoodDto(),
                comida = comidas.randomOrNull() ?: FoodDto(),
                cena = cenas.randomOrNull() ?: FoodDto(),
                habito = habitos[diaIndex % habitos.size],
                completado = false
            )
        }
    }
}

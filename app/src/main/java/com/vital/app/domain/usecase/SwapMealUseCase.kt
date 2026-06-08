package com.vital.app.domain.usecase

import com.vital.app.data.model.DailyPlanDto
import com.vital.app.data.model.FoodDto
import com.vital.app.data.repository.CatalogRepository
import com.vital.app.data.repository.PlanRepository
import com.vital.app.domain.model.UserProfile
import kotlin.math.abs

/**
 * Caso de uso: cambia UNA comida específica (desayuno, comida o cena) por una
 * alternativa del catálogo con calorías similares (±15%).
 * No toca las otras comidas del día.
 */
class SwapMealUseCase(
    private val catalogRepository: CatalogRepository,
    private val planRepository: PlanRepository
) {
    // Tolerancia de calorías para considerar dos alimentos "similares"
    private val CALORIE_TOLERANCE = 0.15

    suspend operator fun invoke(
        uid: String,
        fecha: String,
        tipoComida: String,       // "desayuno", "comida" o "cena"
        comidaActual: FoodDto,
        diaActual: DailyPlanDto,
        profile: UserProfile
    ) {
        // 1. Obtenemos candidatos del mismo tipo de comida, sin alérgenos del usuario
        val candidatos = catalogRepository.getAlimentos(tipoComida, profile.alergias)
            .filter { food ->
                food.nombre != comidaActual.nombre &&  // No repetir la misma
                esSimilarEnCalorias(food.calorias, comidaActual.calorias)
            }

        if (candidatos.isEmpty()) {
            throw NoSuchElementException("No hay alternativas disponibles con calorías similares (±15%) para ${comidaActual.nombre}.")
        }

        val nueva = candidatos.random()

        // 2. Construimos el día actualizado solo tocando la comida que corresponde
        val (nuevoDesayuno, nuevaComida, nuevaCena) = when (tipoComida) {
            "desayuno" -> Triple(nueva, diaActual.comida, diaActual.cena)
            "comida"   -> Triple(diaActual.desayuno, nueva, diaActual.cena)
            else       -> Triple(diaActual.desayuno, diaActual.comida, nueva)
        }

        planRepository.updateDayMeals(uid, fecha, nuevoDesayuno, nuevaComida, nuevaCena)
    }

    private fun esSimilarEnCalorias(a: Int, b: Int): Boolean {
        if (b == 0) return a == 0
        return abs(a - b).toDouble() / b <= CALORIE_TOLERANCE
    }
}

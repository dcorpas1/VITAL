package com.vital.app.domain.usecase

import com.vital.app.data.model.FoodDto
import com.vital.app.data.repository.CatalogRepository
import com.vital.app.data.repository.PlanRepository
import com.vital.app.domain.model.UserProfile

/**
 * Caso de uso: regenera el menú COMPLETO de un día (desayuno + comida + cena)
 * manteniendo el entrenamiento intacto.
 * Respeta los alérgenos del usuario.
 */
class ResetDayMealUseCase(
    private val catalogRepository: CatalogRepository,
    private val planRepository: PlanRepository
) {
    suspend operator fun invoke(uid: String, fecha: String, profile: UserProfile) {
        val desayunos = catalogRepository.getAlimentos("desayuno", profile.alergias)
        val comidas   = catalogRepository.getAlimentos("comida",   profile.alergias)
        val cenas     = catalogRepository.getAlimentos("cena",     profile.alergias)

        val nuevoDesayuno = desayunos.randomOrNull() ?: FoodDto()
        val nuevaComida   = comidas.randomOrNull()   ?: FoodDto()
        val nuevaCena     = cenas.randomOrNull()     ?: FoodDto()

        planRepository.updateDayMeals(uid, fecha, nuevoDesayuno, nuevaComida, nuevaCena)
    }
}

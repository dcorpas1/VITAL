package com.vital.app.domain.usecase

import com.vital.app.domain.model.NutritionResult
import com.vital.app.domain.model.UserProfile

class VitalMotorUseCase {

    fun calcular(profile: UserProfile): NutritionResult {

        // Paso 1 — TMB con Mifflin-St Jeor según sexo
        val tmb = if (profile.sexo == "hombre") {
            10 * profile.peso + 6.25 * profile.altura - 5 * profile.edad + 5
        } else {
            10 * profile.peso + 6.25 * profile.altura - 5 * profile.edad - 161
        }

        // Paso 2 — TDEE: TMB × factor de actividad
        val factorActividad = when (profile.actividad) {
            "sedentario"  -> 1.2
            "ligero"      -> 1.375
            "moderado"    -> 1.55
            "activo"      -> 1.725
            "muy activo"  -> 1.9
            else          -> 1.2
        }
        val tdee = tmb * factorActividad

        // Paso 3 — Ajuste calórico según objetivo
        val caloriasObjetivo = when (profile.objetivo) {
            "perder peso"   -> tdee - 500
            "ganar músculo" -> tdee + 300
            else            -> tdee
        }

        // Paso 4 — Distribución de macros
        val proteinas = profile.peso * 2.0          // 2g por kg
        val grasas = caloriasObjetivo * 0.25 / 9    // 25% de calorías en grasas
        val carbohidratos = (caloriasObjetivo - proteinas * 4 - grasas * 9) / 4

        return NutritionResult(
            tmb = tmb,
            tdee = tdee,
            caloriasObjetivo = caloriasObjetivo,
            proteinas = proteinas,
            grasas = grasas,
            carbohidratos = carbohidratos
        )
    }
}
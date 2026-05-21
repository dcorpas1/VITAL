package com.vital.app.data.remote

import com.vital.app.data.model.UserProfileDto
import com.vital.app.domain.model.NutritionResult
import com.vital.app.domain.model.UserProfile

fun UserProfile.toDto(nutrition: NutritionResult): UserProfileDto {
    return UserProfileDto(
        uid = uid,
        nombre = nombre,
        edad = edad,
        peso = peso,
        altura = altura,
        sexo = sexo,
        objetivo = objetivo,
        actividad = actividad,
        alergias = alergias,
        material = material,
        onboardingCompletado = onboardingCompletado,
        tmb = nutrition.tmb,
        tdee = nutrition.tdee,
        caloriasObjetivo = nutrition.caloriasObjetivo,
        proteinas = nutrition.proteinas,
        grasas = nutrition.grasas,
        carbohidratos = nutrition.carbohidratos
    )
}

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        uid = uid,
        nombre = nombre,
        edad = edad,
        peso = peso,
        altura = altura,
        sexo = sexo,
        objetivo = objetivo,
        actividad = actividad,
        alergias = alergias,
        material = material,
        onboardingCompletado = onboardingCompletado
    )
}
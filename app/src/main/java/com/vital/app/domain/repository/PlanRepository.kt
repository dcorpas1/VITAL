package com.vital.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.data.model.DailyPlanDto
import com.vital.app.data.model.FoodDto
import kotlinx.coroutines.tasks.await

class PlanRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun savePlan(uid: String, plan: List<DailyPlanDto>): Result<Unit> {
        return try {
            val batch = firestore.batch()
            val planesRef = firestore.collection("users")
                .document(uid)
                .collection("planes")

            plan.forEach { dia ->
                val docRef = planesRef.document(dia.fecha)
                batch.set(docRef, dia)
            }

            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPlan(uid: String): Result<List<DailyPlanDto>> {
        return try {
            val docs = firestore.collection("users")
                .document(uid)
                .collection("planes")
                .orderBy("fecha")
                .get()
                .await()
                .toObjects(DailyPlanDto::class.java)
            Result.success(docs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun marcarDiaCompletado(uid: String, fecha: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(uid)
                .collection("planes")
                .document(fecha)
                .update("completado", true)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza las tres comidas de un día concreto en Firestore.
     * Se usa tanto por ResetDayMealUseCase (reseteo completo) como por SwapMealUseCase (swap individual).
     */
    suspend fun updateDayMeals(
        uid: String,
        fecha: String,
        desayuno: FoodDto,
        comida: FoodDto,
        cena: FoodDto
    ): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(uid)
                .collection("planes")
                .document(fecha)
                .update(
                    mapOf(
                        "desayuno" to desayuno,
                        "comida"   to comida,
                        "cena"     to cena
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

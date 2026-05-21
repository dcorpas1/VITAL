package com.vital.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.data.model.DailyPlanDto
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
}
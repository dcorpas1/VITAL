package com.vital.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.data.model.ProgressDto
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProgressRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun registrarPeso(uid: String, peso: Double): Result<Unit> {
        return try {
            val fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val registro = ProgressDto(
                id = fecha,
                fecha = fecha,
                pesoActual = peso
            )
            firestore.collection("users")
                .document(uid)
                .collection("progreso")
                .document(fecha)
                .set(registro)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRegistros(uid: String): Result<List<ProgressDto>> {
        return try {
            val docs = firestore.collection("users")
                .document(uid)
                .collection("progreso")
                .orderBy("fecha")
                .get()
                .await()
                .toObjects(ProgressDto::class.java)
            Result.success(docs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
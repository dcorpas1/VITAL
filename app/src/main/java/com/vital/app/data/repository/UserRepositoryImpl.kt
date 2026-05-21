package com.vital.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.data.model.UserProfileDto
import com.vital.app.data.remote.toDomain
import com.vital.app.data.remote.toDto
import com.vital.app.domain.model.NutritionResult
import com.vital.app.domain.model.UserProfile
import com.vital.app.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserRepository {

    override suspend fun saveProfile(profile: UserProfile): Result<Unit> {
        return try {
            // Calculamos la nutrición antes de guardar
            // (el ViewModel ya nos la pasará, pero la firma de la interfaz
            // la recibe como parte del perfil extendido via DTO)
            firestore.collection("users")
                .document(profile.uid)
                .set(profile.toDto(NutritionResult(0.0,0.0,0.0,0.0,0.0,0.0)))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Versión extendida que incluye el resultado nutricional
    suspend fun saveProfileWithNutrition(
        profile: UserProfile,
        nutrition: NutritionResult
    ): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(profile.uid)
                .set(profile.toDto(nutrition))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(uid: String): Result<UserProfile> {
        return try {
            val doc = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            val dto = doc.toObject(UserProfileDto::class.java)
                ?: return Result.failure(Exception("Perfil no encontrado"))
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
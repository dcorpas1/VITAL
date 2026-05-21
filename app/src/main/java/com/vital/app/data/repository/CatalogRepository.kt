package com.vital.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.data.model.ExerciseDto
import com.vital.app.data.model.FoodDto
import kotlinx.coroutines.tasks.await

class CatalogRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getEjercicios(material: String): List<ExerciseDto> {
        return try {
            firestore.collection("catalog")
                .document("ejercicios")
                .collection("items")
                .whereEqualTo("material", material)
                .get()
                .await()
                .toObjects(ExerciseDto::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAlimentos(tipoComida: String, alergenos: List<String>): List<FoodDto> {
        return try {
            val todos = firestore.collection("catalog")
                .document("alimentos")
                .collection("items")
                .get()
                .await()
                .toObjects(FoodDto::class.java)

            // Filtramos localmente por tipo de comida y alérgenos
            todos.filter { food ->
                food.tipoComida == tipoComida &&
                        food.alergenos.none { it in alergenos }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
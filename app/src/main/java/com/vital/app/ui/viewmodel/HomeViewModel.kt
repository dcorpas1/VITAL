package com.vital.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vital.app.data.model.UserProfileDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class HomeUiState(
    val isLoading: Boolean = true,
    val nombre: String = "",
    val objetivo: String = "",
    val caloriasObjetivo: Double = 0.0,
    val tmb: Double = 0.0,
    val tdee: Double = 0.0,
    val proteinas: Double = 0.0,
    val carbohidratos: Double = 0.0,
    val grasas: Double = 0.0,
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        cargarPerfil()
    }

    private fun cargarPerfil() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val doc = firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()
                val dto = doc.toObject(UserProfileDto::class.java)
                if (dto != null) {
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        nombre = dto.nombre,
                        objetivo = dto.objetivo,
                        caloriasObjetivo = dto.caloriasObjetivo,
                        tmb = dto.tmb,
                        tdee = dto.tdee,
                        proteinas = dto.proteinas,
                        carbohidratos = dto.carbohidratos,
                        grasas = dto.grasas
                    )
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
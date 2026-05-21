package com.vital.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.data.model.ExtendedProfileDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ExtendedProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class ExtendedProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(ExtendedProfileUiState())
    val uiState: StateFlow<ExtendedProfileUiState> = _uiState

    fun guardarPerfilExtendido(
        diasEntrenamiento: Int,
        estiloEntrenamiento: String,
        musculosPrioritarios: List<String>,
        horasSueno: Int,
        nivelEstres: String,
        alergias: List<String>,
        intolerancias: List<String>,
        pesoObjetivo: Double,
        tiempoObjetivo: Int
    ) {
        val uid = auth.currentUser?.uid ?: return
        _uiState.value = ExtendedProfileUiState(isLoading = true)

        val dto = ExtendedProfileDto(
            uid = uid,
            diasEntrenamiento = diasEntrenamiento,
            estiloEntrenamiento = estiloEntrenamiento,
            musculosPrioritarios = musculosPrioritarios,
            horasSueno = horasSueno,
            nivelEstres = nivelEstres,
            alergias = alergias,
            intolerancias = intolerancias,
            pesoObjetivo = pesoObjetivo,
            tiempoObjetivo = tiempoObjetivo,
            perfilExtendidoCompletado = true
        )

        viewModelScope.launch {
            try {
                // Guardamos en subcolección /users/{uid}/perfil/extendido
                firestore.collection("users")
                    .document(uid)
                    .collection("perfil")
                    .document("extendido")
                    .set(dto)
                    .await()

                // Marcamos en el documento principal que el perfil extendido está completado
                firestore.collection("users")
                    .document(uid)
                    .update("perfilExtendidoCompletado", true)
                    .await()

                _uiState.value = ExtendedProfileUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = ExtendedProfileUiState(error = e.message)
            }
        }
    }
}
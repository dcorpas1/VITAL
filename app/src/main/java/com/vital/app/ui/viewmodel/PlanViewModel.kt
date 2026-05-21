package com.vital.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vital.app.data.model.DailyPlanDto
import com.vital.app.data.repository.PlanRepository
import com.vital.app.data.repository.UserRepositoryImpl
import com.vital.app.domain.usecase.GeneratePlanUseCase
import com.vital.app.data.remote.toDomain
import com.vital.app.data.model.UserProfileDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

data class PlanUiState(
    val isLoading: Boolean = true,
    val plan: List<DailyPlanDto> = emptyList(),
    val diaSeleccionado: DailyPlanDto? = null,
    val error: String? = null
)

class PlanViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val planRepository = PlanRepository()
    private val generatePlanUseCase = GeneratePlanUseCase()

    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState

    init {
        cargarOGenerarPlan()
    }

    private fun cargarOGenerarPlan() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                withTimeout(15000L) { // 15 segundos máximo
                    val resultado = planRepository.getPlan(uid)

                    if (resultado.isSuccess && resultado.getOrNull()!!.isNotEmpty()) {
                        _uiState.value = PlanUiState(
                            isLoading = false,
                            plan = resultado.getOrNull()!!
                        )
                    } else {
                        generarPlan(uid)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PlanUiState(
                    isLoading = false,
                    error = "Error cargando el plan: ${e.message}"
                )
            }
        }
    }

    private suspend fun generarPlan(uid: String) {
        try {
            // Leemos el perfil del usuario desde Firestore
            val doc = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            val dto = doc.toObject(UserProfileDto::class.java)
                ?: throw Exception("Perfil no encontrado")

            val profile = dto.toDomain()

            // Generamos el plan con el Motor VITAL + catálogo
            val plan = generatePlanUseCase.execute(profile)

            // Guardamos en Firestore
            planRepository.savePlan(uid, plan)

            _uiState.value = PlanUiState(isLoading = false, plan = plan)

        } catch (e: Exception) {
            _uiState.value = PlanUiState(isLoading = false, error = e.message)
        }
    }

    fun seleccionarDia(dia: DailyPlanDto) {
        _uiState.value = _uiState.value.copy(diaSeleccionado = dia)
    }

    fun cerrarDetalle() {
        _uiState.value = _uiState.value.copy(diaSeleccionado = null)
    }

    fun marcarCompletado() {
        val uid = auth.currentUser?.uid ?: return
        val dia = _uiState.value.diaSeleccionado ?: return

        viewModelScope.launch {
            planRepository.marcarDiaCompletado(uid, dia.fecha)
            // Actualizamos el estado local
            val planActualizado = _uiState.value.plan.map {
                if (it.fecha == dia.fecha) it.copy(completado = true) else it
            }
            _uiState.value = _uiState.value.copy(
                plan = planActualizado,
                diaSeleccionado = dia.copy(completado = true)
            )
        }
    }

    fun regenerarPlan() {
        val uid = auth.currentUser?.uid ?: return
        _uiState.value = PlanUiState(isLoading = true)
        viewModelScope.launch {
            generarPlan(uid)
        }
    }

}
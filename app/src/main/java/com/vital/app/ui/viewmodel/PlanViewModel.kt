package com.vital.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.data.model.DailyPlanDto
import com.vital.app.data.model.FoodDto
import com.vital.app.data.model.UserProfileDto
import com.vital.app.data.remote.toDomain
import com.vital.app.data.repository.PlanRepository
import com.vital.app.domain.model.UserProfile
import com.vital.app.domain.usecase.GeneratePlanUseCase
import com.vital.app.domain.usecase.ResetDayMealUseCase
import com.vital.app.domain.usecase.SwapMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

data class PlanUiState(
    val isLoading: Boolean = true,
    val plan: List<DailyPlanDto> = emptyList(),
    val diaSeleccionado: DailyPlanDto? = null,
    val error: String? = null
)

// Estado para las operaciones de comida (reset y swap)
sealed class MealActionState {
    object Idle : MealActionState()
    data class Loading(val tipo: String = "") : MealActionState() // qué comida está procesando
    object Success : MealActionState()
    data class Error(val message: String) : MealActionState()
}

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val planRepository: PlanRepository,
    private val generatePlanUseCase: GeneratePlanUseCase,
    private val resetDayMealUseCase: ResetDayMealUseCase,
    private val swapMealUseCase: SwapMealUseCase
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState: StateFlow<PlanUiState> = _uiState

    private val _mealActionState = MutableStateFlow<MealActionState>(MealActionState.Idle)
    val mealActionState: StateFlow<MealActionState> = _mealActionState

    init {
        cargarOGenerarPlan()
    }

    private fun cargarOGenerarPlan() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                withTimeout(15000L) {
                    val resultado = planRepository.getPlan(uid)
                    if (resultado.isSuccess && resultado.getOrNull()!!.isNotEmpty()) {
                        _uiState.value = PlanUiState(isLoading = false, plan = resultado.getOrNull()!!)
                    } else {
                        generarPlan(uid)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PlanUiState(isLoading = false, error = "Error cargando el plan: ${e.message}")
            }
        }
    }

    private suspend fun generarPlan(uid: String) {
        try {
            val doc = firestore.collection("users").document(uid).get().await()
            val dto = doc.toObject(UserProfileDto::class.java) ?: throw Exception("Perfil no encontrado")
            val profile = dto.toDomain()
            val plan = generatePlanUseCase.execute(profile)
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
        _mealActionState.value = MealActionState.Idle
    }

    fun marcarCompletado() {
        val uid = auth.currentUser?.uid ?: return
        val dia = _uiState.value.diaSeleccionado ?: return

        viewModelScope.launch {
            planRepository.marcarDiaCompletado(uid, dia.fecha)
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
        viewModelScope.launch { generarPlan(uid) }
    }

    /**
     * Resetea el menú COMPLETO del día seleccionado (desayuno + comida + cena).
     */
    fun resetearMenuDelDia() {
        val uid = auth.currentUser?.uid ?: return
        val dia = _uiState.value.diaSeleccionado ?: return

        _mealActionState.value = MealActionState.Loading("menu")
        viewModelScope.launch {
            try {
                // Cargamos el perfil para respetar alérgenos
                val doc = firestore.collection("users").document(uid).get().await()
                val profile = doc.toObject(UserProfileDto::class.java)?.toDomain()
                    ?: UserProfile()

                resetDayMealUseCase(uid, dia.fecha, profile)

                // Recargamos el día actualizado desde Firestore para reflejar cambios
                recargarDia(uid, dia.fecha)
                _mealActionState.value = MealActionState.Success
            } catch (e: Exception) {
                _mealActionState.value = MealActionState.Error("No se pudo regenerar el menú.")
            }
        }
    }

    /**
     * Cambia UNA comida específica por una alternativa con calorías similares (±15%).
     * @param tipoComida "desayuno", "comida" o "cena"
     * @param comidaActual el FoodDto que se quiere cambiar
     */
    fun cambiarComida(tipoComida: String, comidaActual: FoodDto) {
        val uid = auth.currentUser?.uid ?: return
        val dia = _uiState.value.diaSeleccionado ?: return

        _mealActionState.value = MealActionState.Loading(tipoComida)
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                val profile = doc.toObject(UserProfileDto::class.java)?.toDomain()
                    ?: UserProfile()

                swapMealUseCase(uid, dia.fecha, tipoComida, comidaActual, dia, profile)

                recargarDia(uid, dia.fecha)
                _mealActionState.value = MealActionState.Success
            } catch (e: NoSuchElementException) {
                _mealActionState.value = MealActionState.Error(e.message ?: "Sin alternativas disponibles.")
            } catch (e: Exception) {
                _mealActionState.value = MealActionState.Error("Error al cambiar la comida.")
            }
        }
    }

    fun clearMealActionState() {
        _mealActionState.value = MealActionState.Idle
    }

    /** Recarga un día concreto desde Firestore y actualiza el estado local */
    private suspend fun recargarDia(uid: String, fecha: String) {
        try {
            val doc = firestore.collection("users")
                .document(uid)
                .collection("planes")
                .document(fecha)
                .get()
                .await()
            val diaActualizado = doc.toObject(DailyPlanDto::class.java) ?: return

            val planActualizado = _uiState.value.plan.map {
                if (it.fecha == fecha) diaActualizado else it
            }
            _uiState.value = _uiState.value.copy(
                plan = planActualizado,
                diaSeleccionado = diaActualizado
            )
        } catch (_: Exception) { }
    }
}

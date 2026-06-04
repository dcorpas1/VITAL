package com.vital.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vital.app.data.model.ProgressDto
import com.vital.app.data.repository.ProgressRepository
import com.vital.app.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProgressUiState(
    val isLoading: Boolean = false,
    val pesoInput: String = "",
    val registros: List<ProgressDto> = emptyList(),
    val diasCompletados: Int = 0,
    val rachaActual: Int = 0,
    val mensaje: String? = null,
    val error: String? = null
)

@HiltViewModel
class ProgressViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val progressRepository = ProgressRepository()
    private val planRepository = PlanRepository()

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            // Cargamos registros de peso
            val registros = progressRepository.getRegistros(uid)
            if (registros.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    registros = registros.getOrNull()!!.reversed()
                )
            }

            // Cargamos estadísticas del plan
            val plan = planRepository.getPlan(uid)
            if (plan.isSuccess) {
                val dias = plan.getOrNull()!!
                val completados = dias.count { it.completado }

                // Calculamos racha actual
                var racha = 0
                for (dia in dias.sortedByDescending { it.fecha }) {
                    if (dia.completado) racha++ else break
                }

                _uiState.value = _uiState.value.copy(
                    diasCompletados = completados,
                    rachaActual = racha
                )
            }
        }
    }

    fun updatePesoInput(peso: String) {
        _uiState.value = _uiState.value.copy(pesoInput = peso, mensaje = null)
    }

    fun registrarPeso() {
        val uid = auth.currentUser?.uid ?: return
        val peso = _uiState.value.pesoInput.toDoubleOrNull() ?: return

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val result = progressRepository.registrarPeso(uid, peso)
            if (result.isSuccess) {
                val registros = progressRepository.getRegistros(uid)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    pesoInput = "",
                    mensaje = "✓ Peso registrado correctamente",
                    registros = registros.getOrNull()?.reversed() ?: emptyList()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al guardar"
                )
            }
        }
    }
}
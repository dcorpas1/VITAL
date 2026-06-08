package com.vital.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vital.app.data.repository.UserRepositoryImpl
import com.vital.app.domain.model.UserProfile
import com.vital.app.domain.usecase.VitalMotorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: UserRepositoryImpl,
    private val vitalMotor: VitalMotorUseCase
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    fun completarOnboarding(
        nombre: String,
        edad: Int,
        peso: Double,
        altura: Double,
        sexo: String,
        objetivo: String,
        actividad: String,
        alergias: List<String>,
        material: String
    ) {
        val uid = auth.currentUser?.uid ?: return

        val profile = UserProfile(
            uid = uid,
            nombre = nombre,
            edad = edad,
            peso = peso,
            altura = altura,
            sexo = sexo,
            objetivo = objetivo,
            actividad = actividad,
            alergias = alergias,
            material = material,
            onboardingCompletado = true
        )

        val nutrition = vitalMotor.calcular(profile)
        _uiState.value = OnboardingUiState(isLoading = true)

        viewModelScope.launch {
            val result = repository.saveProfileWithNutrition(profile, nutrition)
            _uiState.value = if (result.isSuccess) {
                OnboardingUiState(isSuccess = true)
            } else {
                OnboardingUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }
}

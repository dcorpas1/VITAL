package com.vital.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vital.app.navigation.Screen
import com.vital.app.ui.theme.*
import com.vital.app.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var step by remember { mutableStateOf(0) }
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("hombre") }
    var objetivo by remember { mutableStateOf("perder peso") }
    var actividad by remember { mutableStateOf("sedentario") }
    var material by remember { mutableStateOf("gimnasio") }

    val totalSteps = 5

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Onboarding.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(VitalRedDark, VitalBlack)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "VITAL",
                color = VitalWhite,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed,
                letterSpacing = 6.sp
            )
            Text(
                "PASO ${step + 1} DE $totalSteps",
                color = VitalTextSecondary,
                fontSize = 12.sp,
                fontFamily = BarlowCondensed,
                letterSpacing = 2.sp
            )

            LinearProgressIndicator(
                progress = { (step + 1) / totalSteps.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = VitalRed,
                trackColor = VitalGrayMid
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (step) {
                0 -> {
                    Text("¿CÓMO TE LLAMAS?", color = VitalWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                    VitalTextField(value = nombre, onValueChange = { nombre = it }, label = "NOMBRE")
                }
                1 -> {
                    Text("DATOS FÍSICOS", color = VitalWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                    VitalTextField(value = edad, onValueChange = { edad = it }, label = "EDAD (AÑOS)")
                    VitalTextField(value = peso, onValueChange = { peso = it }, label = "PESO (KG)")
                    VitalTextField(value = altura, onValueChange = { altura = it }, label = "ALTURA (CM)")
                    Text("SEXO", color = VitalTextSecondary, fontSize = 12.sp, fontFamily = BarlowCondensed, letterSpacing = 2.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("hombre", "mujer").forEach { s ->
                            FilterChip(
                                selected = sexo == s,
                                onClick = { sexo = s },
                                label = {
                                    Text(
                                        s.uppercase(),
                                        fontFamily = BarlowCondensed,
                                        letterSpacing = 1.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VitalRed,
                                    selectedLabelColor = VitalWhite,
                                    labelColor = VitalTextSecondary
                                )
                            )
                        }
                    }
                }
                2 -> {
                    Text("¿CUÁL ES TU OBJETIVO?", color = VitalWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                    listOf("perder peso", "mantener peso", "ganar músculo").forEach { obj ->
                        FilterChip(
                            selected = objetivo == obj,
                            onClick = { objetivo = obj },
                            label = {
                                Text(
                                    obj.uppercase(),
                                    fontFamily = BarlowCondensed,
                                    letterSpacing = 1.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VitalRed,
                                selectedLabelColor = VitalWhite,
                                labelColor = VitalTextSecondary
                            )
                        )
                    }
                }
                3 -> {
                    Text("NIVEL DE ACTIVIDAD", color = VitalWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                    listOf("sedentario", "ligero", "moderado", "activo", "muy activo").forEach { act ->
                        FilterChip(
                            selected = actividad == act,
                            onClick = { actividad = act },
                            label = {
                                Text(
                                    act.uppercase(),
                                    fontFamily = BarlowCondensed,
                                    letterSpacing = 1.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VitalRed,
                                selectedLabelColor = VitalWhite,
                                labelColor = VitalTextSecondary
                            )
                        )
                    }
                }
                4 -> {
                    Text("MATERIAL DISPONIBLE", color = VitalWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                    listOf("gimnasio", "casa").forEach { mat ->
                        FilterChip(
                            selected = material == mat,
                            onClick = { material = mat },
                            label = {
                                Text(
                                    mat.uppercase(),
                                    fontFamily = BarlowCondensed,
                                    letterSpacing = 1.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VitalRed,
                                selectedLabelColor = VitalWhite,
                                labelColor = VitalTextSecondary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (step > 0) {
                    OutlinedButton(
                        onClick = { step-- },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VitalRed)
                    ) {
                        Text("ATRÁS", color = VitalWhite, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                    }
                }

                Button(
                    onClick = {
                        val puedeAvanzar = when (step) {
                            0 -> nombre.isNotBlank()
                            1 -> edad.isNotBlank() && peso.isNotBlank() && altura.isNotBlank()
                            else -> true
                        }
                        if (!puedeAvanzar) return@Button

                        if (step < totalSteps - 1) {
                            step++
                        } else {
                            viewModel.completarOnboarding(
                                nombre = nombre,
                                edad = edad.toIntOrNull() ?: 25,
                                peso = peso.toDoubleOrNull() ?: 70.0,
                                altura = altura.toDoubleOrNull() ?: 170.0,
                                sexo = sexo,
                                objetivo = objetivo,
                                actividad = actividad,
                                alergias = emptyList(),
                                material = material
                            )
                        }
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VitalRed),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading)
                        CircularProgressIndicator(color = VitalWhite, modifier = Modifier.size(20.dp))
                    else
                        Text(
                            if (step < totalSteps - 1) "SIGUIENTE" else "EMPEZAR",
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 2.sp
                        )
                }
            }

            uiState.error?.let {
                Text(it, color = VitalRedLight, fontSize = 13.sp, fontFamily = BarlowCondensed)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun VitalTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, color = VitalTextSecondary, fontFamily = BarlowCondensed, letterSpacing = 1.sp, fontSize = 12.sp)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VitalRed,
            unfocusedBorderColor = VitalGrayLight,
            focusedTextColor = VitalWhite,
            unfocusedTextColor = VitalWhite
        )
    )
}
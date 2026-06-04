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
import com.vital.app.ui.viewmodel.ExtendedProfileViewModel
import com.vital.app.ui.theme.BarlowCondensed
import com.vital.app.ui.theme.VitalBlack
import com.vital.app.ui.theme.VitalRed
import com.vital.app.ui.theme.VitalRedDark
import com.vital.app.ui.theme.VitalRedLight
import com.vital.app.ui.theme.VitalGrayMid
import com.vital.app.ui.theme.VitalGrayLight
import com.vital.app.ui.theme.VitalWhite
import com.vital.app.ui.theme.VitalTextSecondary

@Composable
fun ExtendedProfileScreen(
    navController: NavController,
    viewModel: ExtendedProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var step by remember { mutableStateOf(0) }
    val totalSteps = 4

    var diasEntrenamiento by remember { mutableStateOf(3) }
    var estilosEntrenamiento by remember { mutableStateOf(listOf("hipertrofia")) }
    var musculosPrioritarios by remember { mutableStateOf(listOf<String>()) }
    var horasSueno by remember { mutableStateOf(7) }
    var nivelesEstres by remember { mutableStateOf(listOf("medio")) }
    var alergias by remember { mutableStateOf(listOf<String>()) }
    var intolerancias by remember { mutableStateOf(listOf<String>()) }
    var pesoObjetivo by remember { mutableStateOf("") }
    var tiempoObjetivo by remember { mutableStateOf(3) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.ExtendedProfile.route) { inclusive = true }
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

            // Header — igual que OnboardingScreen
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

            // Título de sección
            Text(
                "PERSONALIZACIÓN AVANZADA",
                color = VitalTextSecondary,
                fontSize = 11.sp,
                fontFamily = BarlowCondensed,
                letterSpacing = 2.sp
            )

            when (step) {
                0 -> {
                    Text(
                        "¿CUÁNTOS DÍAS ENTRENAS?",
                        color = VitalWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(2, 3, 4, 5, 6).forEach { dias ->
                            FilterChip(
                                selected = diasEntrenamiento == dias,
                                onClick = { diasEntrenamiento = dias },
                                label = {
                                    Text(
                                        "$dias DÍAS",
                                        fontFamily = BarlowCondensed,
                                        letterSpacing = 1.sp,
                                        fontSize = 12.sp
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VitalRed,
                                    selectedLabelColor = VitalWhite,
                                    labelColor = VitalTextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "ESTILO DE ENTRENAMIENTO",
                        color = VitalWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "PUEDES ELEGIR VARIOS",
                        color = VitalTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp
                    )
                    listOf("fuerza", "hipertrofia", "resistencia", "cardio", "funcional").forEach { estilo ->
                        val seleccionado = estilo in estilosEntrenamiento
                        FilterChip(
                            selected = seleccionado,
                            onClick = {
                                estilosEntrenamiento = if (seleccionado)
                                    estilosEntrenamiento - estilo
                                else
                                    estilosEntrenamiento + estilo
                            },
                            label = {
                                Text(
                                    estilo.uppercase(),
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

                1 -> {
                    Text(
                        "MÚSCULOS PRIORITARIOS",
                        color = VitalWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "SELECCIONA LOS QUE QUIERAS",
                        color = VitalTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp
                    )
                    listOf(
                        "pecho", "espalda", "hombros", "brazos",
                        "piernas", "glúteos", "abdomen", "gemelos"
                    ).forEach { musculo ->
                        val seleccionado = musculo in musculosPrioritarios
                        FilterChip(
                            selected = seleccionado,
                            onClick = {
                                musculosPrioritarios = if (seleccionado)
                                    musculosPrioritarios - musculo
                                else
                                    musculosPrioritarios + musculo
                            },
                            label = {
                                Text(
                                    musculo.uppercase(),
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

                2 -> {
                    Text(
                        "HORAS DE SUEÑO",
                        color = VitalWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 6, 7, 8, 9).forEach { horas ->
                            FilterChip(
                                selected = horasSueno == horas,
                                onClick = { horasSueno = horas },
                                label = {
                                    Text(
                                        "${horas}H",
                                        fontFamily = BarlowCondensed,
                                        letterSpacing = 1.sp
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VitalRed,
                                    selectedLabelColor = VitalWhite,
                                    labelColor = VitalTextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "NIVEL DE ESTRÉS DIARIO",
                        color = VitalWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "PUEDES ELEGIR VARIOS",
                        color = VitalTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp
                    )
                    listOf("bajo", "medio", "alto", "variable").forEach { estres ->
                        val seleccionado = estres in nivelesEstres
                        FilterChip(
                            selected = seleccionado,
                            onClick = {
                                nivelesEstres = if (seleccionado)
                                    nivelesEstres - estres
                                else
                                    nivelesEstres + estres
                            },
                            label = {
                                Text(
                                    estres.uppercase(),
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
                    Text(
                        "ALERGIAS E INTOLERANCIAS",
                        color = VitalWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "SELECCIONA LAS QUE APLIQUEN (OPCIONAL)",
                        color = VitalTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp
                    )
                    listOf(
                        "gluten", "lactosa", "huevo", "frutos secos",
                        "pescado", "marisco", "soja", "ninguna"
                    ).forEach { alergia ->
                        val seleccionado = alergia in alergias
                        FilterChip(
                            selected = seleccionado,
                            onClick = {
                                alergias = if (seleccionado)
                                    alergias - alergia
                                else
                                    alergias + alergia
                            },
                            label = {
                                Text(
                                    alergia.uppercase(),
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "PESO OBJETIVO (KG)",
                        color = VitalWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.sp
                    )
                    VitalTextField(
                        value = pesoObjetivo,
                        onValueChange = { pesoObjetivo = it },
                        label = "EJ: 75"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "¿EN CUÁNTOS MESES?",
                        color = VitalWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1, 2, 3, 6, 12).forEach { meses ->
                            FilterChip(
                                selected = tiempoObjetivo == meses,
                                onClick = { tiempoObjetivo = meses },
                                label = {
                                    Text(
                                        "${meses}M",
                                        fontFamily = BarlowCondensed,
                                        letterSpacing = 1.sp
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VitalRed,
                                    selectedLabelColor = VitalWhite,
                                    labelColor = VitalTextSecondary
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botones de navegación — mismo estilo que OnboardingScreen
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
                        Text(
                            "ATRÁS",
                            color = VitalWhite,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 1.sp
                        )
                    }
                } else {
                    // Botón volver al perfil solo en el primer paso
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VitalGrayLight)
                    ) {
                        Text(
                            "CANCELAR",
                            color = VitalTextSecondary,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Button(
                    onClick = {
                        if (step < totalSteps - 1) {
                            step++
                        } else {
                            viewModel.guardarPerfilExtendido(
                                diasEntrenamiento = diasEntrenamiento,
                                estiloEntrenamiento = estilosEntrenamiento.joinToString(","),
                                musculosPrioritarios = musculosPrioritarios,
                                horasSueno = horasSueno,
                                nivelEstres = nivelesEstres.joinToString(","),
                                alergias = alergias,
                                intolerancias = intolerancias,
                                pesoObjetivo = pesoObjetivo.toDoubleOrNull() ?: 0.0,
                                tiempoObjetivo = tiempoObjetivo
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VitalRed),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = VitalWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            if (step < totalSteps - 1) "SIGUIENTE" else "GUARDAR",
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }

            uiState.error?.let {
                Text(
                    it,
                    color = VitalRedLight,
                    fontSize = 13.sp,
                    fontFamily = BarlowCondensed
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vital.app.navigation.Screen
import com.vital.app.ui.viewmodel.ExtendedProfileViewModel
import com.vital.app.ui.theme.BarlowCondensed
import com.vital.app.ui.theme.VitalBlack
import com.vital.app.ui.theme.VitalRed
import com.vital.app.ui.theme.VitalRedDark
import com.vital.app.ui.theme.VitalRedLight
import com.vital.app.ui.theme.VitalGray
import com.vital.app.ui.theme.VitalGrayMid
import com.vital.app.ui.theme.VitalGrayLight
import com.vital.app.ui.theme.VitalWhite
import com.vital.app.ui.theme.VitalTextSecondary
import com.vital.app.ui.theme.VitalTextMuted
import com.vital.app.ui.theme.VitalSuccess
import com.vital.app.ui.theme.VitalDarkGray
@Composable
fun ExtendedProfileScreen(
    navController: NavController,
    viewModel: ExtendedProfileViewModel = viewModel()
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
            .background(Color(0xFF0D1B2A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {
                    if (step > 0) step-- else navController.popBackStack()
                }) {
                    Text("← Volver", color = Color(0xFF4A90D9))
                }
                Text("Paso ${step + 1} de $totalSteps", color = Color.Gray, fontSize = 13.sp)
            }

            Text("Personalización avanzada", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            LinearProgressIndicator(
                progress = { (step + 1) / totalSteps.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF4A90D9)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (step) {
                0 -> {
                    Text("¿Cuántos días entrenas por semana?", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(2, 3, 4, 5, 6).forEach { dias ->
                            FilterChip(
                                selected = diasEntrenamiento == dias,
                                onClick = { diasEntrenamiento = dias },
                                label = { Text("$dias", fontSize = 13.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4A90D9),
                                    selectedLabelColor = Color.White,
                                    labelColor = Color.Gray
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Estilo de entrenamiento (puedes elegir varios)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Selecciona todos los que apliquen", color = Color.Gray, fontSize = 13.sp)
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
                            label = { Text(estilo.replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4A90D9),
                                selectedLabelColor = Color.White,
                                labelColor = Color.Gray
                            )
                        )
                    }
                }

                1 -> {
                    Text("¿Qué músculos quieres priorizar?", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Selecciona todos los que quieras", color = Color.Gray, fontSize = 13.sp)
                    listOf("pecho", "espalda", "hombros", "brazos", "piernas", "glúteos", "abdomen", "gemelos").forEach { musculo ->
                        val seleccionado = musculo in musculosPrioritarios
                        FilterChip(
                            selected = seleccionado,
                            onClick = {
                                musculosPrioritarios = if (seleccionado)
                                    musculosPrioritarios - musculo
                                else
                                    musculosPrioritarios + musculo
                            },
                            label = { Text(musculo.replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4A90D9),
                                selectedLabelColor = Color.White,
                                labelColor = Color.Gray
                            )
                        )
                    }
                }

                2 -> {
                    Text("Horas de sueño habituales", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5, 6, 7, 8, 9).forEach { horas ->
                            FilterChip(
                                selected = horasSueno == horas,
                                onClick = { horasSueno = horas },
                                label = { Text("${horas}h") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4A90D9),
                                    selectedLabelColor = Color.White,
                                    labelColor = Color.Gray
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Nivel de estrés diario (puedes elegir varios)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                            label = { Text(estres.replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4A90D9),
                                selectedLabelColor = Color.White,
                                labelColor = Color.Gray
                            )
                        )
                    }
                }

                3 -> {
                    Text("Alergias e intolerancias", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Selecciona las que apliquen (opcional)", color = Color.Gray, fontSize = 13.sp)
                    listOf("gluten", "lactosa", "huevo", "frutos secos", "pescado", "marisco", "soja", "ninguna").forEach { alergia ->
                        val seleccionado = alergia in alergias
                        FilterChip(
                            selected = seleccionado,
                            onClick = {
                                alergias = if (seleccionado)
                                    alergias - alergia
                                else
                                    alergias + alergia
                            },
                            label = { Text(alergia.replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4A90D9),
                                selectedLabelColor = Color.White,
                                labelColor = Color.Gray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Peso objetivo (kg)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    VitalTextField(value = pesoObjetivo, onValueChange = { pesoObjetivo = it }, label = "Ej: 75")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("¿En cuántos meses quieres lograrlo?", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(1, 2, 3, 6, 12).forEach { meses ->
                            FilterChip(
                                selected = tiempoObjetivo == meses,
                                onClick = { tiempoObjetivo = meses },
                                label = { Text("${meses}m") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4A90D9),
                                    selectedLabelColor = Color.White,
                                    labelColor = Color.Gray
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones navegación — SIEMPRE visibles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (step > 0) {
                    OutlinedButton(
                        onClick = { step-- },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4A90D9))
                    ) { Text("Atrás", color = Color.White) }
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90D9)),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading)
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    else
                        Text(
                            if (step < totalSteps - 1) "Siguiente" else "Finalizar",
                            fontWeight = FontWeight.Bold
                        )
                }
            }

            uiState.error?.let {
                Text(it, color = Color.Red, fontSize = 13.sp)
            }

            // Espacio extra al final para que el botón no quede tapado
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
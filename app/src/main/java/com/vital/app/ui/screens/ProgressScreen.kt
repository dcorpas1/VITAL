package com.vital.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vital.app.ui.theme.*
import com.vital.app.ui.viewmodel.ProgressViewModel

@Composable
fun ProgressScreen(
    navController: NavController,
    viewModel: ProgressViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = VitalBlack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "MI PROGRESO",
                color = VitalWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed,
                letterSpacing = 2.sp
            )

            // Registrar peso
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = VitalGray)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "REGISTRAR PESO DE HOY",
                        color = VitalTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = uiState.pesoInput,
                            onValueChange = { viewModel.updatePesoInput(it) },
                            label = {
                                Text("KG", color = VitalTextSecondary, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(4.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VitalRed,
                                unfocusedBorderColor = VitalGrayLight,
                                focusedTextColor = VitalWhite,
                                unfocusedTextColor = VitalWhite
                            )
                        )
                        Button(
                            onClick = { viewModel.registrarPeso() },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VitalRed),
                            enabled = !uiState.isLoading
                        ) {
                            Text("GUARDAR", fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                        }
                    }
                    uiState.mensaje?.let {
                        Text(it, color = VitalSuccess, fontSize = 13.sp, fontFamily = BarlowCondensed)
                    }
                }
            }

            // Estadísticas
            Text(
                "ESTADÍSTICAS",
                color = VitalTextSecondary,
                fontSize = 11.sp,
                fontFamily = BarlowCondensed,
                letterSpacing = 2.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("DÍAS\nENTRENADOS", "${uiState.diasCompletados}", Modifier.weight(1f))
                StatCard("RACHA\nACTUAL", "${uiState.rachaActual} DÍAS", Modifier.weight(1f))
            }

            // Historial
            Text(
                "HISTORIAL DE PESO",
                color = VitalTextSecondary,
                fontSize = 11.sp,
                fontFamily = BarlowCondensed,
                letterSpacing = 2.sp
            )

            if (uiState.registros.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = VitalGray)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "AÚN NO HAY REGISTROS",
                            color = VitalTextMuted,
                            fontSize = 13.sp,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 1.sp
                        )
                    }
                }
            } else {
                uiState.registros.forEach { registro ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(containerColor = VitalGray)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(registro.fecha, color = VitalTextSecondary, fontSize = 13.sp, fontFamily = BarlowCondensed)
                            Text(
                                "${registro.pesoActual} KG",
                                color = VitalRed,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = BarlowCondensed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = VitalGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                value,
                color = VitalRed,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed
            )
            Text(
                label,
                color = VitalTextSecondary,
                fontSize = 10.sp,
                fontFamily = BarlowCondensed,
                letterSpacing = 1.sp
            )
        }
    }
}
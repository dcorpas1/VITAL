package com.vital.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vital.app.data.model.DailyPlanDto
import com.vital.app.navigation.Screen
import com.vital.app.ui.theme.*
import com.vital.app.ui.viewmodel.HomeViewModel
import com.vital.app.ui.viewmodel.PlanViewModel
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
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    planViewModel: PlanViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val planState by planViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = VitalBlack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(VitalRedDark, VitalBlack)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                "VITAL",
                                color = VitalWhite,
                                fontSize = 52.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = BarlowCondensed,
                                letterSpacing = 4.sp
                            )
                            Text(
                                "BIENVENIDO, ${uiState.nombre.uppercase()}",
                                color = VitalRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = BarlowCondensed,
                                letterSpacing = 2.sp
                            )
                        }
                        TextButton(onClick = {
                            homeViewModel.signOut()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }) {
                            Text(
                                "SALIR",
                                color = VitalTextSecondary,
                                fontSize = 12.sp,
                                fontFamily = BarlowCondensed,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "OBJETIVO: ${uiState.objetivo.uppercase()}",
                        color = VitalTextSecondary,
                        fontSize = 12.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Banner personalización
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.ExtendedProfile.route) },
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = VitalRedDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "PERSONALIZA TU PLAN",
                                color = VitalWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                fontFamily = BarlowCondensed,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "Completa el perfil avanzado para mayor precisión",
                                color = VitalWhite.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontFamily = BarlowCondensed
                            )
                        }
                        Text("→", color = VitalWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Calorías
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = VitalGray)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "CALORÍAS DIARIAS",
                            color = VitalTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 2.sp
                        )
                        Text(
                            "${uiState.caloriasObjetivo.toInt()} KCAL",
                            color = VitalWhite,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 1.sp
                        )
                        Divider(color = VitalGrayMid, modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem("TMB", "${uiState.tmb.toInt()}")
                            Box(modifier = Modifier.width(1.dp).height(30.dp).background(VitalGrayMid))
                            StatItem("TDEE", "${uiState.tdee.toInt()}")
                        }
                    }
                }

                // Macros
                Text(
                    "MACRONUTRIENTES",
                    color = VitalTextSecondary,
                    fontSize = 11.sp,
                    fontFamily = BarlowCondensed,
                    letterSpacing = 2.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MacroCard("PROTEÍNAS", "${uiState.proteinas.toInt()}G", VitalRed, Modifier.weight(1f))
                    MacroCard("CARBOS", "${uiState.carbohidratos.toInt()}G", VitalGrayLight, Modifier.weight(1f))
                    MacroCard("GRASAS", "${uiState.grasas.toInt()}G", VitalTextSecondary, Modifier.weight(1f))
                }

                Divider(color = VitalGrayMid)

                // Esta semana
                Text(
                    "ESTA SEMANA",
                    color = VitalTextSecondary,
                    fontSize = 11.sp,
                    fontFamily = BarlowCondensed,
                    letterSpacing = 2.sp
                )

                if (planState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VitalRed, modifier = Modifier.size(24.dp))
                    }
                } else {
                    val semana = planState.plan.take(7)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        semana.forEach { dia ->
                            WeekDayCell(
                                dia = dia,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    planViewModel.seleccionarDia(dia)
                                    navController.navigate(Screen.Calendar.route)
                                }
                            )
                        }
                    }
                }

                // Ver plan completo
                Button(
                    onClick = { navController.navigate(Screen.Calendar.route) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VitalGray)
                ) {
                    Text(
                        "VER PLAN MENSUAL COMPLETO",
                        fontWeight = FontWeight.Bold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 1.5.sp,
                        color = VitalWhite
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            color = VitalWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
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

@Composable
fun WeekDayCell(dia: DailyPlanDto, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val bgColor = when {
        dia.completado -> VitalSuccess
        dia.tipoEntrenamiento == "Descanso" -> VitalDarkGray
        else -> VitalGrayMid
    }
    val numero = dia.fecha.takeLast(2)
    val tipo = dia.tipoEntrenamiento.take(3).uppercase()

    Card(
        modifier = modifier.aspectRatio(0.75f).clickable { onClick() },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(numero, color = VitalWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = BarlowCondensed)
            Text(tipo, color = VitalTextSecondary, fontSize = 8.sp, fontFamily = BarlowCondensed, letterSpacing = 0.5.sp)
        }
    }
}

@Composable
fun MacroCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = VitalGray)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = color, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, fontFamily = BarlowCondensed)
            Text(label, color = VitalTextSecondary, fontSize = 10.sp, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
        }
    }
}
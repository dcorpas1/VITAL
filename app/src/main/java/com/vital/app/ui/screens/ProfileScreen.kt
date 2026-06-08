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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.vital.app.navigation.Screen
import com.vital.app.ui.theme.*
import com.vital.app.ui.viewmodel.HomeViewModel
import com.vital.app.ui.viewmodel.PlanViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    planViewModel: PlanViewModel = hiltViewModel()
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
                "MI PERFIL",
                color = VitalWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed,
                letterSpacing = 2.sp
            )

            // Info principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = VitalGray)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "DATOS PERSONALES",
                        color = VitalTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp
                    )
                    ProfileRow("NOMBRE", uiState.nombre.uppercase())
                    ProfileRow("OBJETIVO", uiState.objetivo.uppercase())
                    ProfileRow("CALORÍAS DIARIAS", "${uiState.caloriasObjetivo.toInt()} KCAL")
                    ProfileRow("TMB", "${uiState.tmb.toInt()} KCAL")
                    ProfileRow("TDEE", "${uiState.tdee.toInt()} KCAL")
                }
            }

            // Macros
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = VitalGray)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "MACRONUTRIENTES",
                        color = VitalTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp
                    )
                    ProfileRow("PROTEÍNAS", "${uiState.proteinas.toInt()}G")
                    ProfileRow("CARBOHIDRATOS", "${uiState.carbohidratos.toInt()}G")
                    ProfileRow("GRASAS", "${uiState.grasas.toInt()}G")
                }
            }

            // Acciones
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
                        "ACCIONES",
                        color = VitalTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp
                    )
                    Button(
                        onClick = { navController.navigate(Screen.ExtendedProfile.route) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VitalRed)
                    ) {
                        Text(
                            "FORMULARIO AVANZADO",
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 2.sp
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            planViewModel.regenerarPlan()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Profile.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VitalRed)
                    ) {
                        Text(
                            "REGENERAR PLAN",
                            color = VitalRed,
                            fontFamily = BarlowCondensed,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Cerrar sesión
            OutlinedButton(
                onClick = {
                    viewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, VitalGrayLight)
            ) {
                Text(
                    "CERRAR SESIÓN",
                    color = VitalTextSecondary,
                    fontFamily = BarlowCondensed,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = VitalTextSecondary, fontSize = 12.sp, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
        Text(value, color = VitalWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = BarlowCondensed)
    }
}
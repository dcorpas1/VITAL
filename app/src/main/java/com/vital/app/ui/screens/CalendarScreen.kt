package com.vital.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vital.app.data.model.DailyPlanDto
import com.vital.app.data.model.FoodDto
import com.vital.app.ui.theme.*
import com.vital.app.ui.viewmodel.MealActionState
import com.vital.app.ui.viewmodel.PlanViewModel

@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: PlanViewModel = hiltViewModel()  // hiltViewModel() en lugar de viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val mealActionState by viewModel.mealActionState.collectAsState()

    // Snackbar para errores de swap/reset
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(mealActionState) {
        if (mealActionState is MealActionState.Error) {
            snackbarHostState.showSnackbar(
                message = (mealActionState as MealActionState.Error).message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearMealActionState()
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = VitalBlack,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = VitalGray,
                    contentColor = VitalWhite,
                    shape = RoundedCornerShape(4.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        color = VitalRed,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.diaSeleccionado != null -> {
                    DayDetailView(
                        dia = uiState.diaSeleccionado!!,
                        mealActionState = mealActionState,
                        onClose = { viewModel.cerrarDetalle() },
                        onMarcarCompletado = { viewModel.marcarCompletado() },
                        onResetMenu = { viewModel.resetearMenuDelDia() },
                        onSwapComida = { tipo, food -> viewModel.cambiarComida(tipo, food) }
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { navController.popBackStack() }) {
                                Text("← VOLVER", color = VitalRed, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                            }
                            Text(
                                "PLAN MENSUAL",
                                color = VitalWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = BarlowCondensed,
                                letterSpacing = 2.sp
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            LegendItem(color = VitalSuccess, label = "COMPLETADO")
                            LegendItem(color = VitalGrayMid, label = "ENTRENO")
                            LegendItem(color = VitalDarkGray, label = "DESCANSO")
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(7),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(uiState.plan) { dia ->
                                DayCell(dia = dia, onClick = { viewModel.seleccionarDia(dia) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).background(color, RoundedCornerShape(2.dp)))
        Text(label, color = VitalTextMuted, fontSize = 9.sp, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
    }
}

@Composable
fun DayCell(dia: DailyPlanDto, onClick: () -> Unit) {
    val bgColor = when {
        dia.completado -> VitalSuccess
        dia.tipoEntrenamiento == "Descanso" -> VitalDarkGray
        else -> VitalGrayMid
    }
    val numero = dia.fecha.takeLast(2)

    Card(
        modifier = Modifier.aspectRatio(0.65f).clickable { onClick() },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(numero, color = VitalWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, fontFamily = BarlowCondensed, textAlign = TextAlign.Center)
            Text(dia.tipoEntrenamiento.take(3).uppercase(), color = VitalTextSecondary, fontSize = 9.sp, fontFamily = BarlowCondensed, letterSpacing = 0.5.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun DayDetailView(
    dia: DailyPlanDto,
    mealActionState: MealActionState,
    onClose: () -> Unit,
    onMarcarCompletado: () -> Unit,
    onResetMenu: () -> Unit,
    onSwapComida: (String, FoodDto) -> Unit
) {
    val isMenuLoading = mealActionState is MealActionState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(dia.fecha, color = VitalWhite, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
            TextButton(onClick = onClose) {
                Text("← VOLVER", color = VitalRed, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
            }
        }

        // ── ENTRENAMIENTO ──
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = VitalGray)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ENTRENAMIENTO: ${dia.tipoEntrenamiento.uppercase()}", color = VitalRed, fontWeight = FontWeight.ExtraBold, fontFamily = BarlowCondensed, letterSpacing = 1.sp, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))
                if (dia.tipoEntrenamiento == "Descanso") {
                    Text("DÍA DE DESCANSO — RECUPERA Y DESCANSA BIEN.", color = VitalTextSecondary, fontSize = 13.sp, fontFamily = BarlowCondensed, letterSpacing = 0.5.sp)
                } else if (dia.ejercicios.isEmpty()) {
                    Text("CARGANDO EJERCICIOS...", color = VitalTextSecondary, fontSize = 13.sp, fontFamily = BarlowCondensed)
                } else {
                    dia.ejercicios.forEach { ejercicio ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(ejercicio.nombre.uppercase(), color = VitalWhite, fontSize = 13.sp, fontFamily = BarlowCondensed, modifier = Modifier.weight(1f))
                            Text("${ejercicio.series}x${ejercicio.repeticiones}", color = VitalRed, fontSize = 13.sp, fontFamily = BarlowCondensed, fontWeight = FontWeight.Bold)
                        }
                        Divider(color = VitalGrayMid, thickness = 0.5.dp)
                    }
                }
            }
        }

        // ── MENÚ DEL DÍA ──
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = VitalGray)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

                // Título + botón REGENERAR MENÚ COMPLETO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("MENÚ DEL DÍA", color = VitalRed, fontWeight = FontWeight.ExtraBold, fontFamily = BarlowCondensed, letterSpacing = 1.sp, fontSize = 15.sp)
                    OutlinedButton(
                        onClick = onResetMenu,
                        enabled = !isMenuLoading,
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VitalRed),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        if (isMenuLoading && (mealActionState as? MealActionState.Loading)?.tipo == "menu") {
                            CircularProgressIndicator(color = VitalRed, modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                        } else {
                            Text("↺ REGENERAR", color = VitalRed, fontFamily = BarlowCondensed, fontSize = 11.sp, letterSpacing = 1.sp)
                        }
                    }
                }

                Divider(color = VitalGrayMid, thickness = 0.5.dp)

                // Desayuno con botón swap
                MealRowWithSwap(
                    tipo = "DESAYUNO",
                    food = dia.desayuno,
                    isSwapping = isMenuLoading && (mealActionState as? MealActionState.Loading)?.tipo == "desayuno",
                    onSwap = { onSwapComida("desayuno", dia.desayuno) }
                )
                Divider(color = VitalGrayMid, thickness = 0.5.dp)

                // Comida con botón swap
                MealRowWithSwap(
                    tipo = "COMIDA",
                    food = dia.comida,
                    isSwapping = isMenuLoading && (mealActionState as? MealActionState.Loading)?.tipo == "comida",
                    onSwap = { onSwapComida("comida", dia.comida) }
                )
                Divider(color = VitalGrayMid, thickness = 0.5.dp)

                // Cena con botón swap
                MealRowWithSwap(
                    tipo = "CENA",
                    food = dia.cena,
                    isSwapping = isMenuLoading && (mealActionState as? MealActionState.Loading)?.tipo == "cena",
                    onSwap = { onSwapComida("cena", dia.cena) }
                )
            }
        }

        // ── HÁBITO ──
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = VitalGrayMid)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("HÁBITO DEL DÍA", color = VitalRed, fontWeight = FontWeight.ExtraBold, fontFamily = BarlowCondensed, letterSpacing = 1.sp, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(dia.habito.uppercase(), color = VitalWhite, fontSize = 14.sp, fontFamily = BarlowCondensed, letterSpacing = 0.5.sp)
            }
        }

        // ── COMPLETAR ──
        if (!dia.completado) {
            Button(
                onClick = onMarcarCompletado,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VitalSuccess)
            ) {
                Text("MARCAR DÍA COMO COMPLETADO", fontWeight = FontWeight.ExtraBold, fontFamily = BarlowCondensed, letterSpacing = 2.sp)
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = VitalSuccess)
            ) {
                Text(
                    "DÍA COMPLETADO ✓",
                    color = VitalWhite,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = BarlowCondensed,
                    letterSpacing = 2.sp,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Fila de comida con nombre, calorías y botón de intercambio (⇄).
 */
@Composable
fun MealRowWithSwap(
    tipo: String,
    food: FoodDto,
    isSwapping: Boolean,
    onSwap: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(tipo, color = VitalTextSecondary, fontSize = 10.sp, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
            Text(
                if (food.nombre.isNotEmpty()) food.nombre.uppercase() else "—",
                color = VitalWhite,
                fontSize = 13.sp,
                fontFamily = BarlowCondensed
            )
            if (food.calorias > 0) {
                Text(
                    "${food.calorias} KCAL  ·  P:${food.proteinas.toInt()}g  C:${food.carbohidratos.toInt()}g  G:${food.grasas.toInt()}g",
                    color = VitalTextMuted,
                    fontSize = 10.sp,
                    fontFamily = BarlowCondensed
                )
            }
        }
        // Botón de intercambio por alternativa similar
        IconButton(
            onClick = onSwap,
            enabled = !isSwapping,
            modifier = Modifier.size(36.dp)
        ) {
            if (isSwapping) {
                CircularProgressIndicator(color = VitalRed, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            } else {
                Text(
                    "⇄",
                    color = VitalRed,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MealRow(tipo: String, nombre: String, calorias: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier.weight(1f)) {
            Text(tipo, color = VitalTextSecondary, fontSize = 10.sp, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
            Text(if (nombre.isNotEmpty()) nombre.uppercase() else "—", color = VitalWhite, fontSize = 13.sp, fontFamily = BarlowCondensed)
        }
        if (calorias > 0) {
            Text("${calorias} KCAL", color = VitalRed, fontSize = 12.sp, fontFamily = BarlowCondensed, fontWeight = FontWeight.Bold)
        }
    }
}

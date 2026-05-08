package com.vital.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.navigation.Screen

@Composable
fun OnboardingScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Controlamos en qué paso del formulario estamos (0 a 3)
    var step by remember { mutableStateOf(0) }

    // Datos que va rellenando el usuario a lo largo del onboarding
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("hombre") }
    var objetivo by remember { mutableStateOf("perder peso") }
    var actividad by remember { mutableStateOf("sedentario") }

    // Para mostrar el spinner mientras guardamos en Firestore
    var loading by remember { mutableStateOf(false) }

    val totalSteps = 4

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("VITAL", color = Color(0xFF4A90D9), fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("Paso ${step + 1} de $totalSteps", color = Color.Gray, fontSize = 13.sp)

            // Barra de progreso que avanza con cada paso
            LinearProgressIndicator(
                progress = { (step + 1) / totalSteps.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF4A90D9)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (step) {

                // Paso 1: nombre del usuario
                0 -> {
                    Text("¿Cómo te llamas?", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4A90D9),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                // Paso 2: datos físicos que necesita el Motor VITAL para calcular la TMB
                1 -> {
                    Text("Datos físicos", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = edad,
                        onValueChange = { edad = it },
                        label = { Text("Edad (años)", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4A90D9),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = peso,
                        onValueChange = { peso = it },
                        label = { Text("Peso (kg)", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4A90D9),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = altura,
                        onValueChange = { altura = it },
                        label = { Text("Altura (cm)", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4A90D9),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Text("Sexo:", color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("hombre", "mujer").forEach { s ->
                            FilterChip(
                                selected = sexo == s,
                                onClick = { sexo = s },
                                label = { Text(s.replaceFirstChar { it.uppercase() }) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4A90D9),
                                    selectedLabelColor = Color.White,
                                    labelColor = Color.Gray
                                )
                            )
                        }
                    }
                }

                // Paso 3: objetivo → define el ajuste calórico sobre el TDEE
                2 -> {
                    Text("¿Cuál es tu objetivo?", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    listOf("perder peso", "mantener peso", "ganar músculo").forEach { obj ->
                        FilterChip(
                            selected = objetivo == obj,
                            onClick = { objetivo = obj },
                            label = { Text(obj.replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4A90D9),
                                selectedLabelColor = Color.White,
                                labelColor = Color.Gray
                            )
                        )
                    }
                }

                // Paso 4: nivel de actividad → multiplica la TMB para obtener el TDEE
                3 -> {
                    Text("Nivel de actividad", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    listOf("sedentario", "ligero", "moderado", "activo", "muy activo").forEach { act ->
                        FilterChip(
                            selected = actividad == act,
                            onClick = { actividad = act },
                            label = { Text(act.replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4A90D9),
                                selectedLabelColor = Color.White,
                                labelColor = Color.Gray
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
                // Solo mostramos "Atrás" si no estamos en el primer paso
                if (step > 0) {
                    OutlinedButton(
                        onClick = { step-- },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4A90D9))
                    ) { Text("Atrás") }
                }

                Button(
                    onClick = {
                        if (step < totalSteps - 1) {
                            step++
                        } else {
                            // Último paso: lanzamos el Motor VITAL y guardamos en Firestore
                            loading = true

                            // Convertimos los strings a números (si falla usamos valores por defecto)
                            val pesoD = peso.toDoubleOrNull() ?: 70.0
                            val alturaD = altura.toDoubleOrNull() ?: 170.0
                            val edadI = edad.toIntOrNull() ?: 25

                            // TMB con Mifflin-St Jeor (varía según sexo)
                            val tmb = if (sexo == "hombre")
                                10 * pesoD + 6.25 * alturaD - 5 * edadI + 5
                            else
                                10 * pesoD + 6.25 * alturaD - 5 * edadI - 161

                            // TDEE = TMB × factor según cuánto se mueve el usuario
                            val factorActividad = when (actividad) {
                                "sedentario"  -> 1.2
                                "ligero"      -> 1.375
                                "moderado"    -> 1.55
                                "activo"      -> 1.725
                                "muy activo"  -> 1.9
                                else          -> 1.2
                            }
                            val tdee = tmb * factorActividad

                            // Ajustamos las calorías según el objetivo
                            val caloriasObjetivo = when (objetivo) {
                                "perder peso"   -> tdee - 500  // déficit
                                "ganar músculo" -> tdee + 300  // superávit
                                else            -> tdee         // mantenimiento
                            }

                            // Calculamos los macros a partir de las calorías objetivo
                            val proteinas = pesoD * 2.0  // 2g por kg de peso
                            val grasas = caloriasObjetivo * 0.25 / 9  // 25% de las calorías
                            val carbos = (caloriasObjetivo - proteinas * 4 - grasas * 9) / 4  // resto

                            // Guardamos todo en Firestore bajo /users/{uid}
                            val perfil = hashMapOf(
                                "nombre"               to nombre,
                                "edad"                 to edadI,
                                "peso"                 to pesoD,
                                "altura"               to alturaD,
                                "sexo"                 to sexo,
                                "objetivo"             to objetivo,
                                "actividad"            to actividad,
                                "tmb"                  to tmb,
                                "tdee"                 to tdee,
                                "caloriasObjetivo"     to caloriasObjetivo,
                                "proteinas"            to proteinas,
                                "grasas"               to grasas,
                                "carbohidratos"        to carbos,
                                "onboardingCompletado" to true
                            )

                            db.collection("users").document(uid)
                                .set(perfil)
                                .addOnSuccessListener {
                                    // Si va bien, vamos a Home y limpiamos el backstack
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    }
                                }
                                .addOnFailureListener {
                                    // Si falla, quitamos el spinner para que pueda reintentar
                                    loading = false
                                }
                        }
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90D9)),
                    enabled = !loading
                ) {
                    if (loading)
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    else
                        Text(if (step < totalSteps - 1) "Siguiente" else "Empezar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
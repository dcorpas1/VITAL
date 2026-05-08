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
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vital.app.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid ?: ""

    var nombre by remember { mutableStateOf("") }
    var calorias by remember { mutableStateOf(0.0) }
    var proteinas by remember { mutableStateOf(0.0) }
    var carbos by remember { mutableStateOf(0.0) }
    var grasas by remember { mutableStateOf(0.0) }
    var objetivo by remember { mutableStateOf("") }
    var tmb by remember { mutableStateOf(0.0) }
    var tdee by remember { mutableStateOf(0.0) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                nombre = doc.getString("nombre") ?: ""
                calorias = doc.getDouble("caloriasObjetivo") ?: 0.0
                proteinas = doc.getDouble("proteinas") ?: 0.0
                carbos = doc.getDouble("carbohidratos") ?: 0.0
                grasas = doc.getDouble("grasas") ?: 0.0
                objetivo = doc.getString("objetivo") ?: ""
                tmb = doc.getDouble("tmb") ?: 0.0
                tdee = doc.getDouble("tdee") ?: 0.0
                loading = false
            }
            .addOnFailureListener { loading = false }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Color(0xFF4A90D9),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
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
                    Column {
                        Text("VITAL", color = Color(0xFF4A90D9), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text("Hola, $nombre 👋", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text("Objetivo: $objetivo", color = Color.Gray, fontSize = 13.sp)
                    }
                    TextButton(onClick = {
                        auth.signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }) {
                        Text("Salir", color = Color.Gray, fontSize = 13.sp)
                    }
                }

                Divider(color = Color(0xFF1A3A5C))

                // Calorías objetivo
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3A5C))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Calorías diarias", color = Color.Gray, fontSize = 13.sp)
                        Text(
                            "${calorias.toInt()} kcal",
                            color = Color(0xFF4A90D9),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("TMB", color = Color.Gray, fontSize = 11.sp)
                                Text("${tmb.toInt()} kcal", color = Color.White, fontSize = 13.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("TDEE", color = Color.Gray, fontSize = 11.sp)
                                Text("${tdee.toInt()} kcal", color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // Macros
                Text("Macronutrientes", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MacroCard("Proteínas", "${proteinas.toInt()}g", Color(0xFF4A90D9), Modifier.weight(1f))
                    MacroCard("Carbos", "${carbos.toInt()}g", Color(0xFF2ECC71), Modifier.weight(1f))
                    MacroCard("Grasas", "${grasas.toInt()}g", Color(0xFFE67E22), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Info Motor VITAL
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF112233))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("⚡ Motor VITAL", color = Color(0xFF4A90D9), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tu plan personalizado está generado y listo.", color = Color.Gray, fontSize = 13.sp)
                        Text("Próximamente: rutinas y menús diarios.", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun MacroCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3A5C))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 11.sp)
        }
    }
}
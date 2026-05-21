package com.vital.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.vital.app.navigation.Screen
import com.vital.app.ui.theme.*
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
fun LoginScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(VitalRedDark, VitalBlack, VitalBlack)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Text(
                "V",
                color = VitalRed,
                fontSize = 96.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed,
                letterSpacing = (-2).sp
            )
            Text(
                "VITAL",
                color = VitalWhite,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed,
                letterSpacing = 8.sp
            )
            Text(
                "ENTRENA. COME. EVOLUCIONA.",
                color = VitalTextSecondary,
                fontSize = 11.sp,
                fontFamily = BarlowCondensed,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text("EMAIL", color = VitalTextSecondary, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text("CONTRASEÑA", color = VitalTextSecondary, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VitalRed,
                    unfocusedBorderColor = VitalGrayLight,
                    focusedTextColor = VitalWhite,
                    unfocusedTextColor = VitalWhite
                )
            )

            if (errorMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMsg, color = VitalRedLight, fontSize = 13.sp, fontFamily = BarlowCondensed)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    loading = true
                    errorMsg = ""
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        .addOnFailureListener {
                            errorMsg = "Email o contraseña incorrectos"
                            loading = false
                        }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VitalRed),
                enabled = !loading
            ) {
                if (loading)
                    CircularProgressIndicator(color = VitalWhite, modifier = Modifier.size(20.dp))
                else
                    Text(
                        "INICIAR SESIÓN",
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp,
                        fontSize = 16.sp
                    )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
                Text(
                    "¿NO TIENES CUENTA? REGÍSTRATE",
                    color = VitalTextSecondary,
                    fontFamily = BarlowCondensed,
                    letterSpacing = 1.sp,
                    fontSize = 13.sp
                )
            }
        }
    }
}
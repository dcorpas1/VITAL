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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.vital.app.navigation.Screen
import com.vital.app.ui.theme.*

@Composable
fun RegisterScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(VitalRedDark, VitalBlack, VitalBlack))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "VITAL",
                color = VitalWhite,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed,
                letterSpacing = 8.sp
            )
            Text(
                "CREA TU CUENTA",
                color = VitalRed,
                fontSize = 16.sp,
                fontFamily = BarlowCondensed,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMsg = ""
                },
                label = {
                    Text("EMAIL", color = VitalTextSecondary, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                },
                isError = errorMsg.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VitalRed,
                    unfocusedBorderColor = VitalGrayLight,
                    errorBorderColor = VitalRedLight,
                    focusedTextColor = VitalWhite,
                    unfocusedTextColor = VitalWhite
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMsg = ""
                },
                label = {
                    Text("CONTRASEÑA", color = VitalTextSecondary, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = errorMsg.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VitalRed,
                    unfocusedBorderColor = VitalGrayLight,
                    errorBorderColor = VitalRedLight,
                    focusedTextColor = VitalWhite,
                    unfocusedTextColor = VitalWhite
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMsg = ""
                },
                label = {
                    Text("CONFIRMAR CONTRASEÑA", color = VitalTextSecondary, fontFamily = BarlowCondensed, letterSpacing = 1.sp)
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = errorMsg.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VitalRed,
                    unfocusedBorderColor = VitalGrayLight,
                    errorBorderColor = VitalRedLight,
                    focusedTextColor = VitalWhite,
                    unfocusedTextColor = VitalWhite
                )
            )

            // Mensaje de error con estilo VITAL
            if (errorMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = VitalRedDark.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚠", color = VitalRedLight, fontSize = 14.sp, fontFamily = BarlowCondensed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = errorMsg,
                        color = VitalRedLight,
                        fontSize = 13.sp,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // ── Validaciones en cliente (antes de llamar a Firebase) ──
                    when {
                        email.isBlank() -> {
                            errorMsg = "El campo email no puede estar vacío."
                            return@Button
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            errorMsg = "El formato del email no es válido."
                            return@Button
                        }
                        password.isBlank() -> {
                            errorMsg = "El campo contraseña no puede estar vacío."
                            return@Button
                        }
                        password.length < 6 -> {
                            errorMsg = "La contraseña debe tener al menos 6 caracteres."
                            return@Button
                        }
                        confirmPassword.isBlank() -> {
                            errorMsg = "Confirma tu contraseña antes de continuar."
                            return@Button
                        }
                        password != confirmPassword -> {
                            errorMsg = "Las contraseñas no coinciden. Compruébalas."
                            return@Button
                        }
                    }

                    loading = true
                    errorMsg = ""
                    auth.createUserWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener {
                            navController.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                        .addOnFailureListener { exception ->
                            loading = false
                            // Mensaje específico según el tipo de error de Firebase
                            errorMsg = when (exception) {
                                is FirebaseAuthUserCollisionException ->
                                    "Ya existe una cuenta con este email. ¿Quieres iniciar sesión?"
                                is FirebaseAuthWeakPasswordException ->
                                    "La contraseña es demasiado débil. Usa al menos 6 caracteres."
                                else ->
                                    "Error al crear la cuenta. Comprueba tu conexión e inténtalo de nuevo."
                            }
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
                        "REGISTRARSE",
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = BarlowCondensed,
                        letterSpacing = 2.sp,
                        fontSize = 16.sp
                    )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text(
                    "¿YA TIENES CUENTA? INICIA SESIÓN",
                    color = VitalTextSecondary,
                    fontFamily = BarlowCondensed,
                    letterSpacing = 1.sp,
                    fontSize = 13.sp
                )
            }
        }
    }
}

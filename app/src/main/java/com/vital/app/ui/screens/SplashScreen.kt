package com.vital.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.vital.app.navigation.Screen
import com.vital.app.ui.theme.*
import kotlinx.coroutines.delay
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
fun SplashScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        delay(1800)
        if (auth.currentUser != null) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(VitalBlack, VitalRedDark, VitalBlack)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "V",
                color = VitalRed,
                fontSize = 120.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed,
                letterSpacing = 0.sp
            )
            Text(
                "VITAL",
                color = VitalWhite,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = BarlowCondensed,
                letterSpacing = 6.sp
            )
            Text(
                "ENTRENA. COME. EVOLUCIONA.",
                color = VitalTextSecondary,
                fontSize = 11.sp,
                fontFamily = BarlowCondensed,
                letterSpacing = 3.sp
            )
        }
    }
}
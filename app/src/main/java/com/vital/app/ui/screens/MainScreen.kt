package com.vital.app.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vital.app.navigation.Screen
import com.vital.app.ui.theme.*

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Triple(Screen.Home.route, "INICIO", ""),
        Triple(Screen.Calendar.route, "PLAN", ""),
        Triple("progress", "PROGRESO", ""),
        Triple("profile", "PERFIL", "")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = VitalDarkGray,
        tonalElevation = 0.dp
    ) {
        items.forEach { (route, label, _) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {},
                label = {
                    Text(
                        label,
                        fontSize = 11.sp,
                        fontFamily = BarlowCondensed,
                        fontWeight = if (currentRoute == route) FontWeight.ExtraBold else FontWeight.Normal,
                        letterSpacing = 1.5.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = VitalRed,
                    unselectedTextColor = VitalTextMuted,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
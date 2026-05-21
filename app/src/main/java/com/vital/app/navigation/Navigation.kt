package com.vital.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vital.app.ui.screens.SplashScreen
import com.vital.app.ui.screens.LoginScreen
import com.vital.app.ui.screens.RegisterScreen
import com.vital.app.ui.screens.OnboardingScreen
import com.vital.app.ui.screens.HomeScreen
import com.vital.app.ui.screens.CalendarScreen
import com.vital.app.ui.screens.ExtendedProfileScreen
import com.vital.app.ui.screens.ProgressScreen
import com.vital.app.ui.screens.ProfileScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Calendar : Screen("calendar")

    object Progress : Screen("progress")
    object Profile : Screen("profile")

    object ExtendedProfile : Screen("extended_profile")

}

@Composable
fun VitalNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }
        composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Calendar.route) { CalendarScreen(navController) }
        composable(Screen.Progress.route) { ProgressScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.ExtendedProfile.route) { ExtendedProfileScreen(navController) }    }
}
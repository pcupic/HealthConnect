package hr.ferit.patrikcupic.healthconnect.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val SCREEN_LOGIN = "login"
    const val SCREEN_REGISTER = "register"
}

@Composable
fun NavigationController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SCREEN_LOGIN) {
        composable(Routes.SCREEN_LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.SCREEN_REGISTER) {
            RegisterScreen(navController)
        }
    }
}

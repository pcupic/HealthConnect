package hr.ferit.patrikcupic.healthconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hr.ferit.patrikcupic.healthconnect.ui.LoginScreen
import hr.ferit.patrikcupic.healthconnect.ui.NavigationController
import hr.ferit.patrikcupic.healthconnect.ui.RegisterScreen
import hr.ferit.patrikcupic.healthconnect.ui.theme.HealthConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationController()
        }
    }
}

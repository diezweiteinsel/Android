package de.cau.inf.se.sopro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import de.cau.inf.se.sopro.ui.theme.CivitasAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CivitasAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val windowSize = calculateWindowSizeClass(this)
                    CivitasApp(windowSize = windowSize.widthSizeClass)
                }
            }
        }
    }
}



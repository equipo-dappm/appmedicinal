package com.burelo.appmedicinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.burelo.appmedicinal.ui.AppNavigation
import com.burelo.appmedicinal.ui.theme.NaturaMedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode = remember { mutableStateOf(false) }
            NaturaMedTheme(darkTheme = isDarkMode.value) {
                AppNavigation(
                    isDarkMode = isDarkMode.value,
                    onToggleDarkMode = { isDarkMode.value = !isDarkMode.value }
                )
            }
        }
    }
}
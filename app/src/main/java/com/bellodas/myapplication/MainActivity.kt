package com.bellodas.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bellodas.denunciasapp.DenunciasMainApp
import com.bellodas.denunciasapp.ui.theme.DenunciasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DenunciasTheme {
                DenunciasMainApp()
            }
        }
    }
}

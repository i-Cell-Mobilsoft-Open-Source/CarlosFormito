package com.icell.external.carlosformito.material3demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.icell.external.carlosformito.material3demo.ui.menu.MenuNavigator
import com.icell.external.carlosformito.material3demo.ui.theme.CarlosFormitoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarlosFormitoTheme {
                MenuNavigator()
            }
        }
    }
}

package com.icell.external.carlosformito.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.icell.external.carlosformito.demo.ui.menu.MenuNavigator
import com.icell.external.carlosformito.demo.ui.theme.CarlosTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarlosTheme {
                MenuNavigator()
            }
        }
    }
}

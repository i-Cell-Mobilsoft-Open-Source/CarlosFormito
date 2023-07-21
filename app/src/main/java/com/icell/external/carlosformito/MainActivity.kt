package com.icell.external.carlosformito

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.icell.external.carlosformito.ui.menu.MenuNavigator
import com.icell.external.carlosformito.ui.theme.CarlosTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarlosTheme {
                MenuNavigator()
            }
        }
    }
}

package com.pemmob.mealoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pemmob.mealoo.ui.navigation.MealooNavGraph
import com.pemmob.mealoo.ui.theme.MealooTheme
import com.pemmob.mealoo.ui.theme.WarmCanvas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MealooTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WarmCanvas
                ) {
                    MealooNavGraph()
                }
            }
        }
    }
}
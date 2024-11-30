package com.flasska.iotam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.flasska.iotam.navigation.NavGraph
import com.flasska.iotam.ui.theme.IoTAnalysisAndManagementTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseApp.initializeApp(
            this, FirebaseOptions.Builder()
                .setApplicationId("1:1234567890:android:abc123def456")
                .setDatabaseUrl("URL Realtime Database")
                .build()
        )

        setContent {
            IoTAnalysisAndManagementTheme {
                Scaffold {
                    Box(
                        modifier = Modifier.padding(it)
                    ) {
                        NavGraph()
                    }
                }
            }
        }
    }
}
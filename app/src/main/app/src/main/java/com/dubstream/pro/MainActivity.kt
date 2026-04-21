package com.dubstream.pro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {

    private var isRecording by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "DubStream Pro",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(onClick = {
                            isRecording = !isRecording
                            if (isRecording) {
                                startService(Intent(this@MainActivity, AudioService::class.java))
                            } else {
                                stopService(Intent(this@MainActivity, AudioService::class.java))
                            }
                        }) {
                            Text(if (isRecording) "⏹️ إيقاف التسجيل" else "▶️ بدء التسجيل")
                        }
                    }
                }
            }
        }
    }
}

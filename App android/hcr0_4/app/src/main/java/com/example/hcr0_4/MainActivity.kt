package com.example.hcr0_4

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hcr0_4.ui.theme.Hcr0_4Theme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hcr0_4.ui.Navigation
import com.example.hcr0_4.ui.ScreensViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val PERMISSION_REQUEST_CODE = 100

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
        setContent {
            Hcr0_4Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background ) {
                    val viewModel: ScreensViewModel = viewModel()
                    Navigation(viewModel = viewModel)
                    Log.d("hcr", "MainActivity: onCreate")

                }
            }
        }
    }
}



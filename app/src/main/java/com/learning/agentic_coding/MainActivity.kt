package com.learning.agentic_coding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.learning.agentic_coding.ui.SaaApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Explicitly re-provide the activity-result owner here: LocalizedContent (one layer
            // down inside SaaApp) overrides LocalContext, which breaks the implicit chain in
            // activity-compose 1.13.0 and crashes rememberLauncherForActivityResult callers.
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides this) {
                SaaApp(services = serviceLocator)
            }
        }
    }
}

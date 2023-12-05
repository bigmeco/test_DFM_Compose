package com.bigmeco.testdfm

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import kotlin.reflect.full.createInstance
import org.w3c.dom.Text


interface DynamicFeatureLoader {
    @Composable
    fun createFragment(): @Composable () -> Unit
}

class MainActivity : ComponentActivity() {
    private lateinit var splitInstallManager: SplitInstallManager
    private var isModuleLoaded = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splitInstallManager = SplitInstallManagerFactory.create(this)

        setContent {
            Column {
                if (isModuleLoaded.value) {
                    // Показываем содержимое динамического модуля
                    ShowDynamicFeature().invoke()
                } else {
                    // Кнопка для загрузки динамического модуля

                }
                Button(onClick = { loadAndLaunchModule("dynamicfeature") }) {
                    Text("Load Dynamic Feature")
                }
            }
        }
    }

    private fun loadAndLaunchModule(moduleName: String) {
        val request = SplitInstallRequest.newBuilder()
            .addModule(moduleName)
            .build()

        splitInstallManager.startInstall(request)
            .addOnSuccessListener {
                // Модуль загружен, обновляем состояние
                isModuleLoaded.value = true
            }
            .addOnFailureListener { exception ->
                // Обработка ошибок
            }
    }

    @Composable
    private fun ShowDynamicFeature(): @Composable () -> Unit {
        val dynamicFeatureLoader = remember {
            val dynamicFeatureLoaderClass =
                Class.forName("com.bigmeco.dynamicfeature.DynamicFeatureLoaderImpl").kotlin
            dynamicFeatureLoaderClass.createInstance() as DynamicFeatureLoader

        }

        return dynamicFeatureLoader.createFragment()
    }
}

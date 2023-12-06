package com.bigmeco.testdfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import org.koin.android.ext.android.inject
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.module.Module
import kotlin.reflect.full.createInstance


interface DynamicComposableProvider {
    @Composable
    fun ProvideComposable()
}

interface DynamicKoinProvider {
    fun ProvideKoin(): Module
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
                    ShowDynamicFeature()
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
    private fun ShowDynamicFeature() {
        val dynamicFeatureLoaderClass =
            Class.forName("com.bigmeco.dynamicfeature.DynamicKoinProviderImpl").kotlin
        loadKoinModules((dynamicFeatureLoaderClass.createInstance() as DynamicKoinProvider).ProvideKoin())

        val dynamicComposableProvider: DynamicComposableProvider by inject()
        dynamicComposableProvider.ProvideComposable()
    }
}

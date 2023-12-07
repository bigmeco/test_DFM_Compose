package com.bigmeco.testdfm

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
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
        SplitCompat.installActivity(this)
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT)

        splitInstallManager = SplitInstallManagerFactory.create(this)
        if (splitInstallManager.installedModules.contains("dynamicfeature")) {
            isModuleLoaded.value = true
        }
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
        val mSplitInstallStateUpdatedListener =
            SplitInstallStateUpdatedListener { state ->
                when (state.status()) {
                    SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                        Toast.makeText(this, "REQUIRES_USER_CONFIRMATION", Toast.LENGTH_LONG)
                        Log.i("Toast.makeText", "REQUIRES_USER_CONFIRMATION")
                    }

                    SplitInstallSessionStatus.DOWNLOADING -> {
                        Log.i("Toast.makeText", "DOWNLOADING")
                        val size = state.totalBytesToDownload()
                        val downloaded = state.bytesDownloaded()
                        if (size > 0) {
                            val percentage = (downloaded * 100 / size).toInt()
                            Log.i("Toast.makeText", "DOWNLOADING $percentage")
                        }
                    }

                    SplitInstallSessionStatus.INSTALLING -> {
                        Log.i("Toast.makeText", "INSTALLING")

                    }

                    SplitInstallSessionStatus.DOWNLOADED -> {
                        Log.i("Toast.makeText", "DOWNLOADED")

                    }

                    SplitInstallSessionStatus.INSTALLED -> {
                        isModuleLoaded.value = true
                        Log.i("Toast.makeText", "INSTALLED")
                    }

                    SplitInstallSessionStatus.CANCELED -> {
                        Log.i("Toast.makeText", "CANCELED")
                    }

                    SplitInstallSessionStatus.PENDING -> {

                        Log.i("Toast.makeText", "PENDING")
                    }

                    SplitInstallSessionStatus.FAILED -> {
                        Log.i("Toast.makeText", "FAILED")

                    }
                }
            }

        splitInstallManager.startInstall(request)
            .addOnSuccessListener {
                Log.i("Toast.makeText", "addOnSuccessListener")

                // Модуль загружен, обновляем состояние
            }
            .addOnFailureListener { exception ->
                Log.i("Toast.makeText", "addOnFailureListener")

                // Обработка ошибок
            }
        splitInstallManager.registerListener(mSplitInstallStateUpdatedListener)
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

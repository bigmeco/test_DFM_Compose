package com.bigmeco.dynamicfeature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.bigmeco.testdfm.DynamicComposableProvider

class DynamicComposableProviderImpl : DynamicComposableProvider {
    private val view = View()

    @Composable
    override fun ProvideComposable() {
        view.ModuleSelector()
    }
}

class View {
    @Composable
    fun ModuleSelector() {
        Text("Load Dynamic Feature 1")
    }
}

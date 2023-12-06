package com.bigmeco.dynamicfeature

import androidx.compose.runtime.Composable
import com.bigmeco.testdfm.DynamicComposableProvider
import com.bigmeco.testdfm.DynamicKoinProvider
import org.koin.core.module.Module

class DynamicKoinProviderImpl : DynamicKoinProvider {
    override fun ProvideKoin(): Module = dynamicFeatureModule

}

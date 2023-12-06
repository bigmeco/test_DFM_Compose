package com.bigmeco.dynamicfeature

import com.bigmeco.testdfm.DynamicComposableProvider
import org.koin.dsl.module

val dynamicFeatureModule = module {
    factory<DynamicComposableProvider> { DynamicComposableProviderImpl() }
}
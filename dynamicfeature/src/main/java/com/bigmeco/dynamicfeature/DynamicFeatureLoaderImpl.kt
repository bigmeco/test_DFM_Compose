package com.bigmeco.dynamicfeature

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bigmeco.testdfm.DynamicFeatureLoader


class DynamicFeatureLoaderImpl : DynamicFeatureLoader {
    @Composable
    override fun createFragment(): @Composable () -> Unit = { Text(text = "Hello, Composable!") }
}

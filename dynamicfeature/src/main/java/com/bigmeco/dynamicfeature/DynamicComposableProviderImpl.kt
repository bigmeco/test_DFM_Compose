package com.bigmeco.dynamicfeature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.bigmeco.testdfm.DynamicComposableProvider
import com.bigmeco.testdfm.R

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
        Image(
            modifier = Modifier.background(colorResource(id = R.color.purple_500))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(56.dp),
            painter = rememberAsyncImagePainter("https://www.learningcontainer.com/wp-content/uploads/2020/07/sample-JPG-File-Download-for-Testing.png"),
            contentDescription = null
        )
    }
}

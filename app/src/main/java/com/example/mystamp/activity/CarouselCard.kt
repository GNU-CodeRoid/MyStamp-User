package com.example.mystamp.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import java.lang.Math.abs


@Composable
fun Carousel(
    count: Int,
    parentModifier: Modifier = Modifier.fillMaxWidth().height(500.dp),
    contentWidth: Dp,
    contentHeight: Dp,
    content: @Composable (modifier: Modifier, index: Int) -> Unit) {

    //val listState = rememberLazyListState(Int.MAX_VALUE / 2)
    val listState = rememberLazyListState(count)

    BoxWithConstraints(
        modifier = parentModifier
    ) {
        val halfRowWidth = constraints.maxWidth / 2

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(-contentHeight / 2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                count = count,
                itemContent = { globalIndex ->
                    val scale by remember {
                        derivedStateOf {
                            val currentItem = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == globalIndex } ?: return@derivedStateOf 0.85f
                            (1f - minOf(1f, abs(currentItem.offset + (currentItem.size / 2) - halfRowWidth).toFloat() / halfRowWidth) * 0.25f)
                        }
                    }

                    content(
                        index = globalIndex % count,
                        modifier = Modifier
                            .width(contentWidth)
                            .height(contentHeight)
                            .scale(scale)
                            .zIndex(scale * 10)
                    )
                }
            )
        }
    }
}


@Composable
fun MyComposableContent(item: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(item)
                .crossfade(true)
                .scale(Scale.FILL)
                .build(),
            contentDescription = null
        )
    }
}


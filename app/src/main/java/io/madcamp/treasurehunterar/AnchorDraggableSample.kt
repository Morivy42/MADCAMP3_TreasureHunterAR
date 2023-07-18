package io.madcamp.treasurehunterar

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

//https://fvilarino.medium.com/exploring-jetpack-compose-anchored-draggable-modifier-5fdb21a0c64c
enum class DragAnchors(val fraction: Float) {
    Start(0f),
    OneQuarter(.25f),
    Half(.5f),
    ThreeQuarters(.75f),
    End(1f),
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalDraggableSample(
    modifier: Modifier = Modifier,
    onFABClick: () -> Unit,
) {
    val density = LocalDensity.current
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val velocityThreshold = { with(density) { 100.dp.toPx() } }
    val animationSpec = tween<Float>()
    val state = rememberSaveable(
        density,
        saver = AnchoredDraggableState.Saver(
            animationSpec = animationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
        )
    ) {
        AnchoredDraggableState(
            initialValue = DragAnchors.Half,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            animationSpec = animationSpec,
        )
    }
    val contentSize = 80.dp
    val contentSizePx = with(density) { contentSize.toPx() }
    Box(
        modifier
            .onSizeChanged { layoutSize ->
                val dragEndPoint = layoutSize.width - contentSizePx
                state.updateAnchors(
                    DraggableAnchors {
                        DragAnchors
                            .values()
                            .filterNot { anchor -> anchor == DragAnchors.OneQuarter || anchor == DragAnchors.ThreeQuarters }
                            .forEach { anchor ->
                                anchor at dragEndPoint * anchor.fraction
                            }
                    }
                )
            }
    ) {
        DraggableContent(
            modifier = Modifier
                .size(contentSize)
                .offset {
                    IntOffset(
                        x = state.requireOffset().roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal),
            onFABClick = onFABClick
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalDraggableSample(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val velocityThreshold = { with(density) { 100.dp.toPx() } }
    val animationSpec = tween<Float>()
    val state = rememberSaveable(
        density,
        saver = AnchoredDraggableState.Saver(
            animationSpec = animationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
        )
    ) {
        AnchoredDraggableState(
            initialValue = DragAnchors.Half,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            animationSpec = animationSpec,
        )
    }
    val contentSize = 50.dp
    val contentSizePx = with(density) { contentSize.toPx() }
    Box(
        modifier
            .onSizeChanged { layoutSize ->
                val dragEndPoint = layoutSize.height - contentSizePx
                state.updateAnchors(
                    DraggableAnchors {
                        DragAnchors
                            .values()
                            .forEach { anchor ->
                                anchor at dragEndPoint * anchor.fraction
                            }
                    }
                )
            }
    ) {
        DraggableContent(
            modifier = Modifier
                .size(contentSize)
                .offset {
                    IntOffset(
                        x = 0,
                        y = state.requireOffset().roundToInt(),
                    )
                }
                .anchoredDraggable(state, Orientation.Vertical),
            onFABClick = {}
        )
    }
}

@Composable
fun DraggableContent(
    modifier: Modifier = Modifier,
    onFABClick: () -> Unit
) {
    FloatingActionButton(
        onClick = { onFABClick() },
        modifier = modifier,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "AR FAB"
        )

    }
}
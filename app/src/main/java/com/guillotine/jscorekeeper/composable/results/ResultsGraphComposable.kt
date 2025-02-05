package com.guillotine.jscorekeeper.composable.results

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ResultsGraphComposable(
    scoreChanges: List<Int>,
    color: Color,
    strokeWidth: Float = 3f,
    modifier: Modifier = Modifier
) {
    val listOfScores = mutableListOf<Int>()
    var currentScore = 0
    listOfScores.add(currentScore)
    for (scoreChange in scoreChanges) {
        // Flip everything upside-down, since positive is down and negative is up by default.
        currentScore -= scoreChange
        listOfScores.add(currentScore)
    }
    val maxScore = listOfScores.max()
    val minScore = listOfScores.min()

    Row(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        ) {
            // Normalize to 0-1, then scale to the height.
            val listOfPoints =
                listOfScores.map { ((it.toFloat() - minScore.toFloat()) / (maxScore.toFloat() - minScore.toFloat()) * size.height) }
            val startingY =
                ((0f - minScore.toFloat()) / (maxScore.toFloat() - minScore.toFloat()) * size.height)
            val graphPath = Path().apply {
                moveTo(0f, startingY)
                var currentX = 0f
                // Scale such that the graph fits the width perfectly.
                val xIncrement = size.width / (listOfPoints.size - 1)
                for (point in listOfPoints) {
                    lineTo(currentX, point)
                    currentX += xIncrement
                }
            }
            drawPath(path = graphPath, color = color, style = Stroke(strokeWidth))
        }
    }
}


@Preview
@Composable
fun GraphPreview() {
    ResultsGraphComposable(listOf(100, 200, -300, 100), Color.Black, 3f, Modifier.fillMaxHeight())
}
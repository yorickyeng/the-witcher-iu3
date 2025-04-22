package com.fk.thewitcheriu3.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fk.thewitcheriu3.domain.models.Cell

@Composable
fun CellView(
    cell: Cell,
    selectedCell: Pair<Int, Int>? = null,
    cellsInMoveRange: Set<Pair<Int, Int>> = emptySet(),
    cellsInAttackRange: Set<Pair<Int, Int>> = emptySet(),
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        when {
            selectedCell == Pair(cell.xCoord, cell.yCoord) -> Color.Green
            Pair(cell.xCoord, cell.yCoord) in cellsInAttackRange -> Color.Red
            Pair(cell.xCoord, cell.yCoord) in cellsInMoveRange -> Color.Yellow
            else -> Color.Black
        }, label = "borderColorAnimation"
    )

    Box(modifier = Modifier
        .aspectRatio(1f)
        .border(2.dp, borderColor)
        .clickable { onClick() }) {

        Image(
            painter = painterResource(cell.texture), contentDescription = cell.type
        )

        if (cell.hero != null) {
            Image(
                painter = painterResource(cell.hero.texture),
                contentDescription = cell.hero.getName()
            )
        }
        if (cell.unit != null) {
            Image(
                painter = painterResource(cell.unit.texture),
                contentDescription = cell.unit.getType()
            )
        }
    }
}
package org.example

import pt.isel.canvas.RED


val RACKET_CENTRAL_ZONE = 40
val RACKET_EDGE_ZONE = 10
val RACKET_MIDDLE_ZONE = 15
val RACKET_WIDTH = RACKET_CENTRAL_ZONE + RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE
val RACKET_HEIGHT = 10
val RACKET_COLOR = RED
val RACKET_DEFAULT_Y_CORD = 550
val RACKET_STARTING_POS_X = (WIDTH / 2) - (RACKET_WIDTH / 2)
val RACKET_X_CORD = RACKET_STARTING_POS_X

data class Racket(val x: Int = RACKET_X_CORD, val y: Int = RACKET_DEFAULT_Y_CORD)

fun drawPaddle(paddle: Racket) {
    arena.drawRect(
        x = paddle.x,
        y = paddle.y,
        width = RACKET_WIDTH,
        height = RACKET_HEIGHT,
        color = RACKET_COLOR
    )
}

fun newPaddle(xCord: Int, yCord: Int = RACKET_DEFAULT_Y_CORD): Racket {
    val racketXCordCorrected = if (xCord + RACKET_WIDTH <= WIDTH) xCord else WIDTH - RACKET_WIDTH
    return Racket(racketXCordCorrected, yCord)
}

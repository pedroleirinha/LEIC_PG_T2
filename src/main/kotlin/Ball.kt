package org.example

import pt.isel.canvas.BLUE
import kotlin.random.Random


val BALL_RADIUS = 7
val BALL_COLOR = BLUE
val BALL_GENERATOR_PERIOD = 5000
val MAX_DELTA_X = 5
val MAX_DELTA_Y = 3
val MIN_DELTA_Y = 1

data class Ball(val x: Int = 0, val y: Int = 0, val deltaX: Int = 0, val deltaY: Int = 0)

fun generateRandomBall(): Ball {
    val xCord = Random.nextInt(CANVAS_INVALID_POS_OFFSET, WIDTH - CANVAS_INVALID_POS_OFFSET)
    val yCord = HEIGHT - 30

    val xDelta = Random.nextInt(MAX_DELTA_X)
    val yDelta = Random.nextInt(MIN_DELTA_Y, MAX_DELTA_Y)


    return Ball(xCord, yCord, xDelta, -yDelta)
}

fun drawBalls(ballsList: List<Ball>) {
    ballsList.forEach { arena.drawCircle(it.x, it.y, BALL_RADIUS, BALL_COLOR) }
}

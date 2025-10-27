package org.example

import pt.isel.canvas.BLUE
import kotlin.random.Random


val BALL_RADIUS = 7
val BALL_COLOR = BLUE
val BALL_GENERATOR_PERIOD = 5000
val MAX_DELTA = 7
val MAX_DELTA_Y = 1

data class Ball(val x: Int = 0, val y: Int = 0, val deltaX: Int = 0, val deltaY: Int = 0)

fun generateRandomBall(): Ball {
    val xCord = Random.nextInt(CANVAS_INVALID_POS_OFFSET, WIDTH - CANVAS_INVALID_POS_OFFSET)
    val yCord = HEIGHT - 30

    val xDelta = Random.nextInt(MAX_DELTA)


    return Ball(xCord, yCord, xDelta, -1)
}

fun drawBalls(ballsList: List<Ball>) {
    ballsList.forEach { it -> arena.drawCircle(it.x, it.y, BALL_RADIUS, BALL_COLOR) }

}
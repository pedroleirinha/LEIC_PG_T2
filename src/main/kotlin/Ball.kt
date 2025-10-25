package org.example

import pt.isel.canvas.BLUE
import kotlin.random.Random


val BALL_RADIUS = 7
val BALL_COLOR = BLUE
val BALL_GENERATOR_PERIOD = 5000

data class Ball(val x: Int = 0, val y: Int = 0)

fun generateRandomBall(): Ball {
    val xCord = Random.nextInt(CANVAS_INVALID_POS_OFFSET, WIDTH - CANVAS_INVALID_POS_OFFSET)
    val yCord = Random.nextInt(CANVAS_INVALID_POS_OFFSET, HEIGHT - CANVAS_INVALID_POS_OFFSET)

    return Ball(xCord, yCord)
}

fun drawBalls(ballsList: List<Ball>) {
    ballsList.forEach { it -> arena.drawCircle(it.x, it.y, BALL_RADIUS, BALL_COLOR) }

}
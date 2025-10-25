package org.example

import pt.isel.canvas.*
import kotlin.random.Random

val WIDTH = 600
val HEIGHT = 400
val BACKGROUND_COLOR = BLACK

val CANVAS_INVALID_POS_OFFSET = 10

val BALL_RADIUS = 7
val BALL_COLOR = BLUE
val TIME_TICK_MLS = 10

val RACKET_CENTRAL_ZONE = 40
val RACKET_EDGE_ZONE = 10
val RACKET_MIDDLE_ZONE = 15
val RACKET_WIDTH = RACKET_CENTRAL_ZONE + RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE
val RACKET_HEIGHT = 10
val RACKET_COLOR = RED
val RACKET_Y_CORD = 350
val RACKET_STARTING_POS_X = (WIDTH / 2) - (RACKET_WIDTH / 2)
val RACKET_X_CORD = RACKET_STARTING_POS_X
val RACKET_INVALID_POS_OFFSET = RACKET_WIDTH

val arena = Canvas(WIDTH, HEIGHT, BACKGROUND_COLOR)

data class Area(val width: Int, val height: Int)
data class Racket(val x: Int = RACKET_X_CORD, val y: Int = RACKET_Y_CORD)
data class Ball(val x: Int = 0, val y: Int = 0)


data class Game(val area: Area, val balls: List<Ball>, val racket: Racket)


fun main() {
    val balls: MutableList<Ball> = emptyList<Ball>().toMutableList()

    onStart {
        val paddle = Racket()
        drawPaddle(paddle)

        arena.onTimeProgress(2000) {
            balls.add(generateRandomBall())
        }

        arena.onMouseMove { me ->
            arena.erase()

            val newPaddle =
                Racket(if (me.x <= WIDTH - RACKET_INVALID_POS_OFFSET) me.x else me.x - RACKET_INVALID_POS_OFFSET)
            drawPaddle(newPaddle)
            drawBalls(balls)
        }

        print("START")
    }

    onFinish {
        print("FINISH")

    }
}

fun generateRandomBall(): Ball {
    val xCord = Random.nextInt(CANVAS_INVALID_POS_OFFSET, WIDTH - CANVAS_INVALID_POS_OFFSET)
    val yCord = Random.nextInt(CANVAS_INVALID_POS_OFFSET, HEIGHT - CANVAS_INVALID_POS_OFFSET)

    return Ball(xCord, yCord)
}

fun drawBalls(ballsList: List<Ball>) {
    ballsList.forEach { it -> arena.drawCircle(it.x, it.y, BALL_RADIUS, BALL_COLOR) }

}

fun drawPaddle(paddle: Racket) {
    arena.drawRect(
        x = paddle.x,
        y = RACKET_Y_CORD,
        width = RACKET_WIDTH,
        height = RACKET_HEIGHT,
        color = RACKET_COLOR
    )
}
package org.example

import pt.isel.canvas.*

val WIDTH = 400
val HEIGHT = 600
val BACKGROUND_COLOR = BLACK

val CANVAS_INVALID_POS_OFFSET = 10


val TIME_TICK_MLS = 10

fun main() {
    val balls: MutableList<Ball> = emptyList<Ball>().toMutableList()
    val paddle = Racket()
    val area = Area(WIDTH, HEIGHT)
    var game = Game(area, balls, paddle)

    onStart {
        drawPaddle(paddle)

        arena.onTimeProgress(BALL_GENERATOR_PERIOD) {
            balls.add(generateRandomBall())
            game = Game(area, balls, paddle)
        }

        arena.onTimeProgress(TIME_TICK_MLS) {
            arena.erase()
            drawGame(game)
        }

        arena.onMouseMove { me ->
            val racket = newPaddle(me.x)
            game = Game(area, balls, racket)
        }

        print("START")
    }

    onFinish {
        print("FINISH")

    }
}

fun drawGame(game: Game) {
    drawPaddle(game.racket)
    drawBalls(game.balls)
}
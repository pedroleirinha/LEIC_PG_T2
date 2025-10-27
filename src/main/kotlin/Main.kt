package org.example

import pt.isel.canvas.*

val WIDTH = 400
val HEIGHT = 600
val BACKGROUND_COLOR = BLACK

val CANVAS_INVALID_POS_OFFSET = 10


val TIME_TICK_MLS = 10

fun main() {
    val paddle = Racket()
    val area = Area(WIDTH, HEIGHT)
    var game = Game(area, emptyList(), paddle)

    onStart {
        drawPaddle(paddle)

        arena.onTimeProgress(BALL_GENERATOR_PERIOD) {
            val newBalls: List<Ball> = game.balls + generateRandomBall();
            game = Game(area, newBalls, game.racket)
        }

        arena.onTimeProgress(TIME_TICK_MLS) {
            arena.erase()
            val newBallsUpdated = checkAllBallsPossibleColision(game.balls, game.area)
            val ballsMoved = updateBallsCoords(newBallsUpdated)
            game = Game(area, ballsMoved, game.racket)

            drawGame(game)
        }

        arena.onMouseMove { me ->
            val racket = newPaddle(me.x)
            game = Game(area, game.balls, racket)
        }

        print("START")
    }

    onFinish {
        print("FINISH")

    }
}

fun checkBallColision(ball: Ball, area: Area): Ball {
    if (ball.x <= 10 || ball.x >= area.width - 10) {
        //print("colisao lateral")
        return ball.copy(deltaX = -ball.deltaX)
    } else if (ball.y <= 10 || ball.y >= area.height - 10) {

        //print("colisao vertical")
        return ball.copy(deltaY = -ball.deltaY)
    }
    return ball
}


fun updateBallsCoords(balls: List<Ball>): List<Ball> {
    return balls.map { Ball(it.x + it.deltaX, it.y + it.deltaY, it.deltaX, it.deltaY) }
}

fun checkAllBallsPossibleColision(balls: List<Ball>, area: Area): List<Ball> {
    return balls.map { checkBallColision(it, area) }
}

fun drawGame(game: Game) {
    drawPaddle(game.racket)
    drawBalls(game.balls)
}
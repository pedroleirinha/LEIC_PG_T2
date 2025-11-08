package org.example

import pt.isel.canvas.*
import kotlin.math.sign

const val WIDTH = 400
const val HEIGHT = 600
const val BACKGROUND_COLOR = BLACK

const val CANVAS_INVALID_POS_OFFSET = 10
const val TIME_TICK_MLS = 10

enum class Collision {
    HORIZONTAL,
    VERTICAL,
    NONE
}

enum class DIRECTIONS(value: Int) {
    UP(-1),
    DOWN(1),
    LEFT(-1),
    RIGHT(1)
}

fun main() {
    val paddle = Racket()
    val area = Area(WIDTH, HEIGHT)
    var game = Game(area, emptyList(), paddle)

    onStart {
        drawPaddle(paddle)

        arena.onTimeProgress(BALL_GENERATOR_PERIOD) {
            val newBalls: List<Ball> = game.balls + generateRandomBall()
            game = game.copy(balls = newBalls)
        }

        arena.onTimeProgress(TIME_TICK_MLS) {
            arena.erase()

            val newBallsList = handleGameBallsBehaviour(game)
            game = game.copy(balls = newBallsList)

            drawGame(game)
        }

        arena.onMouseMove { me ->
            val racket = newPaddle(me.x)
            game = game.copy(racket = racket)
        }

        print("START")
    }

    onFinish {
        print("FINISH")
    }
}

fun handleGameBallsBehaviour(game: Game): List<Ball> {
    val filteredBalls = filterBallsOutOfBounds(game.balls, game.area)
    val newBallsUpdated = checkAllBallsPossibleCollision(filteredBalls, game.area, game.racket)
    val ballsMoved = updateBallsCoords(newBallsUpdated)
    return ballsMoved
}

fun checkBallColisionWithArea(ball: Ball, area: Area): Collision {
    val offset = 10
    if (ball.x <= offset || ball.x >= area.width - offset) {
        return Collision.HORIZONTAL
    } else if (ball.y <= offset) {
        return Collision.VERTICAL
    }
    return Collision.NONE
}

fun checkBallCollisionWithRacket(ball: Ball, racket: Racket): Collision {
    val offset = 10

    val horizontalCollision = ball.x in racket.x..(racket.x + RACKET_WIDTH)
    val verticalCollision = (ball.y + offset) in racket.y..(racket.y + RACKET_HEIGHT)

    if (horizontalCollision && verticalCollision && ball.deltaY.sign == DIRECTIONS.DOWN.ordinal) {
        println("Colision with Racket")
        return Collision.VERTICAL
    }

    return Collision.NONE
}

fun updateBallDeltasAfterColision(ball: Ball, typeCollision: Collision): Ball {
    return when (typeCollision) {
        Collision.HORIZONTAL -> ball.copy(deltaX = -ball.deltaX)
        Collision.VERTICAL -> ball.copy(deltaY = -ball.deltaY)
        else -> ball
    }
}

fun updateBallsCoords(balls: List<Ball>): List<Ball> {
    return balls.map { Ball(it.x + it.deltaX, it.y + it.deltaY, it.deltaX, it.deltaY) }
}

fun checkAllBallsPossibleCollision(balls: List<Ball>, area: Area, racket: Racket): List<Ball> {
    return balls.map { checkAndUpdateBallMovementAfterCollision(it, area, racket) }
}

fun filterBallsOutOfBounds(balls: List<Ball>, area: Area): List<Ball> {
    return balls.filter { !checkBallOutOfBounds(it, area) }
}

fun checkBallOutOfBounds(ball: Ball, area: Area): Boolean {
    if (ball.y >= area.height && ball.deltaY.sign == DIRECTIONS.DOWN.ordinal) {
        println("Outofbounds")
        return true
    }
    return false
}

fun checkAndUpdateBallMovementAfterCollision(ball: Ball, area: Area, racket: Racket): Ball {
    val racketCollision = checkBallCollisionWithRacket(ball, racket)
    val areaCollision = checkBallColisionWithArea(ball, area)

    return if (racketCollision != Collision.NONE) {
        updateBallDeltasAfterColision(ball, racketCollision)
    } else if (areaCollision != Collision.NONE) {
        updateBallDeltasAfterColision(ball, areaCollision)
    } else {
        ball
    }
}

fun drawGame(game: Game) {
    drawPaddle(game.racket)
    drawBalls(game.balls)
    drawBallsCounter(game.balls)
}

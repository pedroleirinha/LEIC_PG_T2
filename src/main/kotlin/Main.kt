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
    BOTH,
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
            val racket = newPaddle(me.x - RACKET_WIDTH / 2)
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
        return Collision.BOTH
    }

    return Collision.NONE
}

//Checks where in the racket the collision happens to determine the delta change
fun checkRacketCollisionPosition(ball: Ball, racket: Racket): Int {
    return when {
        ball.x <= racket.x + RACKET_EDGE_ZONE -> -RACKET_EDGE_ZONE_DELTA_CHANGE
        ball.x >= (racket.x + RACKET_WIDTH) - RACKET_EDGE_ZONE -> RACKET_EDGE_ZONE_DELTA_CHANGE

        ball.x <= racket.x + (RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE) -> -RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE
        ball.x >= (racket.x + RACKET_WIDTH) - (RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE) -> RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE

        else -> ball.deltaX
    }
}

fun updateBallDeltasAfterColision(ball: Ball, deltaXChange: Int = 1, deltaYChange: Int = 1): Ball {
    return ball.copy(deltaX = deltaXChange, deltaY = deltaYChange);
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
        val newDeltaX =
            if (racketCollision == Collision.BOTH) checkRacketCollisionPosition(ball, racket) else ball.deltaX
        print("NEW deltaX $newDeltaX")
        val newDeltaY = if (racketCollision == Collision.BOTH) -ball.deltaY else ball.deltaY

        var newBallDeltaX = ball.deltaX + newDeltaX;
        if (ball.deltaX + newDeltaX > 4) {
            newBallDeltaX = 4;
        } else if (ball.deltaX + newDeltaX < -4) {
            newBallDeltaX = -4;
        }
        updateBallDeltasAfterColision(ball, newBallDeltaX, newDeltaY)

    } else if (areaCollision != Collision.NONE) {
        val newDeltaX = if (areaCollision == Collision.HORIZONTAL) -ball.deltaX else ball.deltaX
        val newDeltaY = if (areaCollision == Collision.VERTICAL) -ball.deltaY else ball.deltaY
        updateBallDeltasAfterColision(ball, newDeltaX, newDeltaY)
    } else {
        ball
    }
}

fun drawGame(game: Game) {
    drawPaddle(game.racket)
    drawBalls(game.balls)
    drawBallsCounter(game.balls)
}

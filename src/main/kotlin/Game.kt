package org.example

import pt.isel.canvas.BLACK
import pt.isel.canvas.Canvas
import pt.isel.canvas.ESCAPE_CODE
import pt.isel.canvas.WHITE

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

val arena = Canvas(WIDTH, HEIGHT, BACKGROUND_COLOR)

data class Area(val width: Int, val height: Int)
data class Game(val area: Area, val balls: List<Ball> = emptyList(), val racket: Racket)

fun gameStart() {
    val area = Area(WIDTH, HEIGHT)
    var game = Game(area = area, racket = Racket())

    arena.onTimeProgress(BALL_GENERATOR_PERIOD) {
        game = game.copy(balls = game.balls + generateRandomBall())
    }

    arena.onTimeProgress(TIME_TICK_MLS) {
        arena.erase()
        game = game.copy(balls = handleGameBallsBehaviour(game))
        drawGame(game)
    }

    arena.onMouseMove { me ->
        game = game.copy(racket = game.racket.moveTo(me.x - RACKET_WIDTH / 2))
    }

    arena.onKeyPressed {
        if (it.code == ESCAPE_CODE) arena.close()
    }
}

fun drawBalls(ballsList: List<Ball>) {
    ballsList.forEach { arena.drawCircle(it.x, it.y, BALL_RADIUS, BALL_COLOR) }
}

fun drawBallsCounter(balls: List<Ball>) {
    arena.drawText(WIDTH / 2, BALL_COUNTER_YCORD, balls.size.toString(), WHITE, BALL_COUNT_FONTSIZE)
}

fun handleGameBallsBehaviour(game: Game): List<Ball> {
    val filteredBalls = filterBallsOutOfBounds(game.balls)
    val newBallsUpdated = checkAllBallsPossibleCollision(filteredBalls, game.racket)
    val ballsMoved = updateBallsCoords(newBallsUpdated)
    return ballsMoved
}

fun checkAllBallsPossibleCollision(balls: List<Ball>, racket: Racket) =
    balls.map { checkAndUpdateBallMovementAfterCollision(it, racket) }


fun filterBallsOutOfBounds(balls: List<Ball>) = balls.filter { !it.isOutOfBounds() }

fun checkAndUpdateBallMovementAfterCollision(ball: Ball, racket: Racket): Ball {
    val racketCollision = ball.isCollidingWithRacket(racket)
    val areaCollision = ball.isCollidingWithArea()

    return updateBallMovementAfterCollision(ball, racket, racketCollision, areaCollision)
}

fun drawGame(game: Game) {
    drawPaddle(game.racket)
    drawBalls(game.balls)
    drawBallsCounter(game.balls)
}

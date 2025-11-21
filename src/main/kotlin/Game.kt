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

enum class DIRECTIONS(val value: Int) {
    DOWN(value = 1),
    UP(value = -1),
}

val arena = Canvas(WIDTH, HEIGHT, BACKGROUND_COLOR)

data class Area(val width: Int = WIDTH, val height: Int = HEIGHT)
data class Game(
    val area: Area = Area(),
    val balls: List<Ball> = emptyList(),
    val racket: Racket = Racket()
)

fun gameStart() {
    var game = Game()

    arena.onTimeProgress(period = BALL_GENERATOR_PERIOD) {
        game = game.copy(balls = game.balls + generateRandomBall())
    }

    arena.onTimeProgress(period = TIME_TICK_MLS) {
        arena.erase()
        val updatedBalls = handleGameBallsBehaviour(balls = game.balls, racket = game.racket)
        if (!game.balls.isEmpty() && updatedBalls.isEmpty()) arena.close()
        game = game.copy(balls = updatedBalls)
        drawGame(game)
    }

    arena.onMouseMove { me ->
        game = game.copy(racket = game.racket.moveTo(to = me.x))
    }

    arena.onKeyPressed {
        if (it.code == ESCAPE_CODE) arena.close()
    }
}

fun drawBalls(ballsList: List<Ball>) {
    ballsList.forEach { arena.drawCircle(xCenter = it.x, yCenter = it.y, radius = BALL_RADIUS, color = BALL_COLOR) }
}

fun drawBallsCounter(balls: List<Ball>) {
    arena.drawText(
        x = WIDTH / 2,
        y = BALL_COUNTER_YCORD,
        txt = balls.size.toString(),
        color = WHITE,
        fontSize = BALL_COUNT_FONTSIZE
    )
}

fun handleGameBallsBehaviour(balls: List<Ball>, racket: Racket): List<Ball> {
    val filteredBalls = filterBallsOutOfBounds(balls = balls)
    val newBallsUpdated = checkAllBallsPossibleCollision(balls = filteredBalls, racket = racket)
    val ballsMoved = updateBallsMovement(balls = newBallsUpdated)
    return ballsMoved
}

fun checkAllBallsPossibleCollision(balls: List<Ball>, racket: Racket) =
    balls.map { checkAndUpdateBallMovementAfterCollision(ball = it, racket = racket) }


fun filterBallsOutOfBounds(balls: List<Ball>) = balls.filter { !it.isOutOfBounds() }

fun checkAndUpdateBallMovementAfterCollision(ball: Ball, racket: Racket) =
    updateBallMovementAfterCollision(
        ball = ball,
        racket = racket,
        racketCollision = ball.isCollidingWithRacket(racket = racket),
        areaCollision = ball.isCollidingWithArea()
    )

fun drawGame(game: Game) {
    drawRacket(racket = game.racket)
    drawBalls(ballsList = game.balls)
    drawBallsCounter(balls = game.balls)
}

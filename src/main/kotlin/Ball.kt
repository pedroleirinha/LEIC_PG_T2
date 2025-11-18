package org.example

import pt.isel.canvas.CYAN
import kotlin.math.sign
import kotlin.random.Random


const val BALL_COUNT_FONTSIZE = 30
const val BALL_COUNTER_YCORD = 585
const val BALL_RADIUS = 7
const val BALL_COLOR = CYAN
const val BALL_GENERATOR_PERIOD = 5000
const val MAX_DELTA_X = 6
const val MAX_DELTA_Y = 4
const val MIN_DELTA_Y = 1

data class Ball(val x: Int = 0, val y: Int = 0, val deltaX: Int = 0, val deltaY: Int = 0)

fun generateRandomBall(): Ball {
    val xCord = Random.nextInt(CANVAS_INVALID_POS_OFFSET, WIDTH - CANVAS_INVALID_POS_OFFSET)
    val yCord = HEIGHT - 30

    val xDelta = Random.nextInt(MAX_DELTA_X)
    val yDelta = Random.nextInt(MIN_DELTA_Y, MAX_DELTA_Y)


    return Ball(xCord, yCord, xDelta, -yDelta)
}

fun Ball.isCollidingWithRacket(racket: Racket): Collision {
    val horizontalCollision = this.x + BALL_RADIUS in racket.x..(racket.x + RACKET_WIDTH)
    val verticalCollision = (this.y + BALL_RADIUS) in racket.y..(racket.y + RACKET_HEIGHT)

    return when {
        horizontalCollision && verticalCollision && this.deltaY.sign == DIRECTIONS.DOWN.ordinal -> Collision.BOTH
        horizontalCollision -> Collision.HORIZONTAL
        verticalCollision -> Collision.VERTICAL
        else -> Collision.NONE
    }
}

fun Ball.move() =
    copy(x = this.x + this.deltaX, y = this.y + deltaY)

fun Ball.isCollidingWithArea() = when {
    this.x - BALL_RADIUS <= 0 || this.x + BALL_RADIUS >= WIDTH -> Collision.HORIZONTAL
    this.y - BALL_RADIUS <= 0 -> Collision.VERTICAL
    else -> Collision.NONE
}

fun Ball.adjustDirectionAfterColliding(newDeltaX: Int) = when {
    this.deltaX + newDeltaX > MAX_DELTA_X -> MAX_DELTA_X
    this.deltaX + newDeltaX < -MAX_DELTA_X -> -MAX_DELTA_X
    else -> this.deltaX + newDeltaX
}

fun Ball.isOutOfBounds() = this.y >= HEIGHT && this.deltaY.sign == DIRECTIONS.DOWN.ordinal

fun Ball.updateBallDeltasAfterColision(deltaXChange: Int = 1, deltaYChange: Int = 1) =
    this.copy(deltaX = deltaXChange, deltaY = deltaYChange)

fun updateBallAfterCollisionArea(ball: Ball, areaCollision: Collision): Ball {
    val newDeltaX = if (areaCollision == Collision.HORIZONTAL) -ball.deltaX else ball.deltaX
    val newDeltaY = if (areaCollision == Collision.VERTICAL) -ball.deltaY else ball.deltaY

    return ball.updateBallDeltasAfterColision(newDeltaX, newDeltaY)
}

fun updateBallAfterCollisionRacket(ball: Ball, racket: Racket): Ball {
    val newDeltaX = checkRacketCollisionPosition(ball, racket)
    val newDeltaY = -ball.deltaY

    val newBallDeltaX = ball.adjustDirectionAfterColliding(newDeltaX)

    println("Ball DeltaX ${ball.deltaX} - NEW deltaX $newDeltaX, DELTA after adjustment $newBallDeltaX, BATEU EM ${ball.x - racket.x} ")

    return ball.updateBallDeltasAfterColision(newBallDeltaX, newDeltaY)
}

fun updateBallMovementAfterCollision(
    ball: Ball, racket: Racket, racketCollision: Collision, areaCollision: Collision
) = when {
    racketCollision == Collision.BOTH -> updateBallAfterCollisionRacket(ball, racket)
    areaCollision != Collision.NONE -> updateBallAfterCollisionArea(ball, areaCollision)
    else -> ball
}


fun updateBallsCoords(balls: List<Ball>) = balls.map { it.move() }


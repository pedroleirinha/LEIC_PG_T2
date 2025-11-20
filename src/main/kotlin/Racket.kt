package org.example

const val RACKET_CENTRAL_ZONE = 40
const val RACKET_EDGE_ZONE = 10
const val RACKET_MIDDLE_ZONE = 15
const val RACKET_EDGE_ZONE_DELTA_CHANGE = 3
const val RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE = 1
const val RACKET_WIDTH = RACKET_CENTRAL_ZONE + RACKET_MIDDLE_ZONE * 2 + RACKET_EDGE_ZONE * 2
const val RACKET_HEIGHT = 10

//val RACKET_COLOR = RED
const val RACKET_DEFAULT_Y_CORD = 540
const val RACKET_STARTING_POS_X = (WIDTH / 2) - (RACKET_WIDTH / 2)
const val RACKET_X_CORD = RACKET_STARTING_POS_X
const val RACKET_IMAGE_FILENAME = "arkanoid_racket"

data class Racket(val x: Int = RACKET_X_CORD, val y: Int = RACKET_DEFAULT_Y_CORD)

fun drawRacket(racket: Racket) {

    arena.drawImage(
        fileName = RACKET_IMAGE_FILENAME,
        xLeft = racket.x,
        yTop = racket.y,
        width = RACKET_WIDTH,
        height = RACKET_HEIGHT,
    )

//    arena.drawRect(
//        x = paddle.x,
//        y = paddle.y,
//        width = RACKET_WIDTH,
//        height = RACKET_HEIGHT,
//        color = RACKET_COLOR
//    )
}

fun Racket.newPaddle(xCord: Int, yCord: Int = RACKET_DEFAULT_Y_CORD): Racket {
    val racketXCordCorrected = if (xCord + RACKET_WIDTH <= WIDTH) xCord else WIDTH - RACKET_WIDTH
    return copy(x = racketXCordCorrected, y = yCord)
}

fun Racket.moveTo(to: Int) = this.newPaddle(xCord = to - RACKET_WIDTH / 2)

//Checks where in the racket the collision happens to determine the delta change
fun checkRacketCollisionPosition(ball: Ball, racket: Racket) = when {
    ball.x <= racket.x + RACKET_EDGE_ZONE -> -RACKET_EDGE_ZONE_DELTA_CHANGE
    ball.x >= (racket.x + RACKET_WIDTH) - RACKET_EDGE_ZONE -> RACKET_EDGE_ZONE_DELTA_CHANGE

    ball.x <= racket.x + (RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE) -> -RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE
    ball.x >= (racket.x + RACKET_WIDTH) - (RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE) -> RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE

    else -> 0
}



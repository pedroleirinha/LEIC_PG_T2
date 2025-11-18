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

data class Racket(val x: Int = RACKET_X_CORD, val y: Int = RACKET_DEFAULT_Y_CORD)

fun drawPaddle(paddle: Racket) {

    arena.drawImage(
        "arkanoid_racket",
        xLeft = paddle.x,
        yTop = paddle.y,
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

fun newPaddle(xCord: Int, yCord: Int = RACKET_DEFAULT_Y_CORD): Racket {
    val racketXCordCorrected = if (xCord + RACKET_WIDTH <= WIDTH) xCord else WIDTH - RACKET_WIDTH
    return Racket(racketXCordCorrected, yCord)
}

//Checks where in the racket the collision happens to determine the delta change
fun checkRacketCollisionPosition(ball: Ball, racket: Racket) = when {
    ball.x <= racket.x + RACKET_EDGE_ZONE -> -RACKET_EDGE_ZONE_DELTA_CHANGE
    ball.x >= (racket.x + RACKET_WIDTH) - RACKET_EDGE_ZONE -> RACKET_EDGE_ZONE_DELTA_CHANGE

    ball.x <= racket.x + (RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE) -> -RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE
    ball.x >= (racket.x + RACKET_WIDTH) - (RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE) -> RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE

    else -> 0
}



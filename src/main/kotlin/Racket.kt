package org.example

import pt.isel.canvas.RED
import pt.isel.canvas.WHITE

const val RACKET_CENTRAL_ZONE = 40
const val RACKET_EDGE_ZONE = 10
const val RACKET_MIDDLE_ZONE = 15
const val RACKET_EDGE_ZONE_DELTA_CHANGE = 3
const val RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE = 1
const val RACKET_WIDTH = RACKET_CENTRAL_ZONE + RACKET_MIDDLE_ZONE * 2 + RACKET_EDGE_ZONE * 2
const val RACKET_HEIGHT = 10
const val RACKET_TOP_LAYER_HEIGHT = 5
const val RACKET_BASE_COLOR = WHITE
const val RACKET_EDGES_COLOR = RED
const val RACKET_MIDDLE_EDGE_COLOR = 0xF59827
const val RACKET_DEFAULT_Y_CORD = 540
const val RACKET_STARTING_POS_X = (WIDTH / 2) - (RACKET_WIDTH / 2)
const val RACKET_X_CORD = RACKET_STARTING_POS_X

data class Racket(val x: Int = RACKET_X_CORD, val y: Int = RACKET_DEFAULT_Y_CORD)

fun drawRacketBottomLayer(racket: Racket) {
    arena.drawRect(
        x = racket.x,
        y = racket.y + RACKET_TOP_LAYER_HEIGHT,
        width = RACKET_WIDTH,
        height = RACKET_HEIGHT - RACKET_TOP_LAYER_HEIGHT,
        color = RACKET_BASE_COLOR
    )
}

fun drawRacketEdge(racket: Racket) {
    arena.drawRect(
        x = racket.x,
        y = racket.y,
        width = RACKET_EDGE_ZONE,
        height = RACKET_TOP_LAYER_HEIGHT,
        color = RACKET_EDGES_COLOR
    )

    arena.drawRect(
        x = racket.x + RACKET_WIDTH - RACKET_EDGE_ZONE,
        y = racket.y,
        width = RACKET_EDGE_ZONE,
        height = RACKET_TOP_LAYER_HEIGHT,
        color = RACKET_EDGES_COLOR
    )
}

fun drawRacketMiddleEdge(racket: Racket) {
    arena.drawRect(
        x = racket.x + RACKET_EDGE_ZONE,
        y = racket.y,
        width = RACKET_MIDDLE_ZONE,
        height = RACKET_TOP_LAYER_HEIGHT,
        color = RACKET_MIDDLE_EDGE_COLOR
    )


    arena.drawRect(
        x = racket.x + RACKET_WIDTH - RACKET_EDGE_ZONE - RACKET_MIDDLE_ZONE,
        y = racket.y,
        width = RACKET_MIDDLE_ZONE,
        height = RACKET_TOP_LAYER_HEIGHT,
        color = RACKET_MIDDLE_EDGE_COLOR
    )
}

fun drawRacketCenter(racket: Racket) {
    arena.drawRect(
        x = racket.x + RACKET_EDGE_ZONE + RACKET_MIDDLE_ZONE,
        y = racket.y,
        width = RACKET_CENTRAL_ZONE,
        height = RACKET_TOP_LAYER_HEIGHT,
        color = RACKET_BASE_COLOR
    )
}

fun drawRacketTopLayer(racket: Racket) {
    drawRacketEdge(racket)
    drawRacketMiddleEdge(racket)
    drawRacketCenter(racket)
}

fun drawRacket(racket: Racket) {
    drawRacketBottomLayer(racket)
    drawRacketTopLayer(racket)
}

/*
* Cria uma raquete nas novas coordenadas
*/
fun Racket.newPaddle(xCord: Int, yCord: Int = RACKET_DEFAULT_Y_CORD): Racket {
    val racketXCordCorrected = when {
        xCord + RACKET_WIDTH > WIDTH -> WIDTH - RACKET_WIDTH
        xCord <= 0 -> 0
        else -> xCord
    }
    return copy(x = racketXCordCorrected, y = yCord)
}

/*
* Cria uma raquete após esta ser mexida, atualizando a sua coord X.
*/
fun Racket.moveTo(to: Int) = this.newPaddle(xCord = to - RACKET_WIDTH / 2)

//Checks where in the racket the collision happens to determine the delta change
fun checkRacketCollisionPosition(ball: Ball, racket: Racket) = when {
    ball.x <= racket.x + RACKET_EDGE_ZONE -> -RACKET_EDGE_ZONE_DELTA_CHANGE
    ball.x >= (racket.x + RACKET_WIDTH) - RACKET_EDGE_ZONE -> RACKET_EDGE_ZONE_DELTA_CHANGE

    ball.x <= racket.x + (RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE) -> -RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE
    ball.x >= (racket.x + RACKET_WIDTH) - (RACKET_MIDDLE_ZONE + RACKET_EDGE_ZONE) -> RACKET_MIDDLE_EDGE_ZONE_DELTA_CHANGE

    else -> 0
}



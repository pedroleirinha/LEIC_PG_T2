package org.example

import pt.isel.canvas.Canvas


val arena = Canvas(WIDTH, HEIGHT, BACKGROUND_COLOR)

data class Area(val width: Int, val height: Int)

data class Game(val area: Area, val balls: List<Ball>, val racket: Racket)


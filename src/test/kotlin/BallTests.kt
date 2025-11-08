import org.example.Area
import org.example.Ball
import org.example.Collision
import org.example.HEIGHT
import org.example.MAX_DELTA_X
import org.example.MAX_DELTA_Y
import org.example.MIN_DELTA_Y
import org.example.Racket
import org.example.WIDTH
import org.example.checkAndUpdateBallMovementAfterCollision
import org.example.checkBallColisionWithArea
import org.example.checkBallCollisionWithRacket
import org.example.checkRacketCollisionPosition
import org.example.generateRandomBall
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BallTests {

    @Test
    fun testBallGeneration() {
        val testBall = generateRandomBall()

        assertTrue(testBall.x in 0..<WIDTH)
        assertTrue(testBall.y in 0..<HEIGHT)
        assertTrue(testBall.deltaX in 0..<MAX_DELTA_X)
        assertTrue(testBall.deltaY in -MAX_DELTA_Y..-MIN_DELTA_Y)

        val testBall2 = generateRandomBall()

        assertFalse(testBall2.x !in 0..<WIDTH)
        assertFalse(testBall2.y !in 0..<HEIGHT)
        assertFalse(testBall2.deltaX !in 0..<MAX_DELTA_X)
        assertFalse(testBall2.deltaY !in (-MAX_DELTA_Y..<-MIN_DELTA_Y))
    }

    @Test
    fun testBallCollisions() {
        val racket = Racket(0, 400)
        val area = Area(500, 600)

        val ball = Ball(100, 100, 1, -1)

        var racketCollision = checkBallCollisionWithRacket(ball, racket)
        var areaCollision = checkBallColisionWithArea(ball, area)

        assertEquals(Collision.NONE, racketCollision)
        assertEquals(Collision.NONE, areaCollision)

        val ball2 = Ball(0, 300, -1, -1)

        racketCollision = checkBallCollisionWithRacket(ball2, racket)
        areaCollision = checkBallColisionWithArea(ball2, area)

        assertEquals(Collision.NONE, racketCollision)
        assertEquals(Collision.HORIZONTAL, areaCollision)

        val ball3 = Ball(5, 400, 1, 1)

        racketCollision = checkBallCollisionWithRacket(ball3, racket)
        areaCollision = checkBallColisionWithArea(ball3, area)

        assertEquals(Collision.BOTH, racketCollision)
        assertEquals(Collision.HORIZONTAL, areaCollision)

        val ball4 = Ball(5, 400, 1, -1)

        racketCollision = checkBallCollisionWithRacket(ball4, racket)
        areaCollision = checkBallColisionWithArea(ball4, area)

        assertEquals(Collision.NONE, racketCollision)
        assertEquals(Collision.HORIZONTAL, areaCollision)
    }

    @Test
    fun testBallDeltaChangeAfterCollisions() {
        val racket = Racket(0, 400)
        val area = Area(500, 600)

        val ball = Ball(100, 100, 1, -1)
        val newBall = checkAndUpdateBallMovementAfterCollision(ball, area, racket)

        assertEquals(newBall.deltaX, 1)
        assertEquals(newBall.deltaY, -1)

        val ball2 = Ball(0, 300, -1, -1)
        val newBall2 = checkAndUpdateBallMovementAfterCollision(ball2, area, racket)

        assertEquals(newBall2.deltaX, 1)
        assertEquals(newBall2.deltaY, -1)

        val ball3 = Ball(5, 400, 1, 1)
        val newBall3 = checkAndUpdateBallMovementAfterCollision(ball3, area, racket)

        assertEquals(newBall3.deltaX, -2)
        assertEquals(newBall3.deltaY, -1)
    }

    @Test
    fun testBallDeltaChangeOnRacketCollision() {
        val racket = Racket(100, 400)
        val ball = Ball(140, 400, 1, -1)

        var deltaX = checkRacketCollisionPosition(ball, racket)
        assertEquals(deltaX, 1)

        val ball2 = Ball(105, 400, 1, -1)
        deltaX = checkRacketCollisionPosition(ball2, racket)
        assertEquals(deltaX, -3)

        val ball3 = Ball(115, 400, 1, -1)
        deltaX = checkRacketCollisionPosition(ball3, racket)
        assertEquals(deltaX, -1)


        val ball4 = Ball(185, 400, 1, -1)
        deltaX = checkRacketCollisionPosition(ball4, racket)
        assertEquals(deltaX, 3)

        val ball5 = Ball(169, 400, 1, -1)
        deltaX = checkRacketCollisionPosition(ball5, racket)
        assertEquals(deltaX, 1)

        val ball6 = Ball(164, 400, 1, -1)
        deltaX = checkRacketCollisionPosition(ball6, racket)
        assertNotEquals(deltaX, 3)
        assertEquals(deltaX, 1)



    }
}

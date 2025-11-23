import org.example.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BallTests {

    @Test
    fun testBallGeneration() {

        repeat(5) {
            val testBall = generateRandomBall()

            assertTrue(testBall.x in 0..<WIDTH)
            assertTrue(testBall.y in 0..<HEIGHT)
            assertTrue(testBall.deltaX in -MAX_DELTA_X..MAX_DELTA_X)
            assertTrue(testBall.deltaY in -MAX_DELTA_Y..-MIN_DELTA_Y)
        }

    }

    @Test
    fun testBallCollisions() {
        val racket = Racket(0, 400)

        val ball = Ball(100, 100, 1, -1)

        var racketCollision = ball.isCollidingWithRacket(racket)
        var areaCollision = ball.isCollidingWithArea()

        assertEquals(Collision.NONE, racketCollision)
        assertEquals(Collision.NONE, areaCollision)

        val ball2 = Ball(0, 300, -1, -1)

        racketCollision = ball2.isCollidingWithRacket(racket)
        areaCollision = ball2.isCollidingWithArea()

        assertNotEquals(Collision.BOTH, racketCollision)
        assertEquals(Collision.HORIZONTAL, areaCollision)

        val ball3 = Ball(5, 400, 1, 1)

        racketCollision = ball3.isCollidingWithRacket(racket)
        areaCollision = ball3.isCollidingWithArea()

        assertEquals(Collision.BOTH, racketCollision)
        assertEquals(Collision.HORIZONTAL, areaCollision)

        val ball4 = Ball(5, 400, 1, -1)

        racketCollision = ball4.isCollidingWithRacket(racket)
        areaCollision = ball4.isCollidingWithArea()

        assertNotEquals(Collision.NONE, racketCollision)
        assertEquals(Collision.HORIZONTAL, areaCollision)


        val ball5 = Ball(5, 400, 1, 1)

        racketCollision = ball5.isCollidingWithRacket(racket)
        areaCollision = ball5.isCollidingWithArea()

        assertEquals(Collision.BOTH, racketCollision)
        assertEquals(Collision.HORIZONTAL, areaCollision)
    }

    @Test
    fun testBallCollisionOnRacketEdge() {
        val racket = Racket(310, RACKET_DEFAULT_Y_CORD)
        val ball = Ball(x = 389, y = RACKET_DEFAULT_Y_CORD, deltaX = 0, deltaY = 1)

        val collision = ball.isCollidingWithRacket(racket)
        assertEquals(Collision.BOTH, collision)
    }

    @Test
    fun testBallDeltaChangeAfterCollisions() {
        val racket = Racket(0, 400)

        val ball = Ball(100, 100, 1, -1)
        val newBall = checkAndUpdateBallMovementAfterCollision(ball, racket)

        assertEquals(newBall.deltaX, 1)
        assertEquals(newBall.deltaY, -1)

        val ball2 = Ball(0, 200, -1, -1)
        val newBall2 = checkAndUpdateBallMovementAfterCollision(ball2, racket)

        assertEquals(newBall2.deltaX, 1)
        assertEquals(newBall2.deltaY, -1)

        val ball3 = Ball(5, 400, 1, 1)
        val newBall3 = checkAndUpdateBallMovementAfterCollision(ball3, racket)

        assertEquals(newBall3.deltaX, -2)
        assertEquals(newBall3.deltaY, -1)
    }

    @Test
    fun testBallDeltaChangeOnRacketCollision() {
        val racket = Racket(100, 400)
        val ball = Ball(140, 400, 1, -1)

        var deltaX = checkRacketCollisionPosition(ball, racket)
        assertEquals(deltaX, 0)

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

        val ball6 = Ball(170, 400, 1, 5)
        deltaX = checkRacketCollisionPosition(ball6, racket)
        assertNotEquals(deltaX, 3)
        assertEquals(deltaX, 1)


    }
}

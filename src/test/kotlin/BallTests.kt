import org.example.HEIGHT
import org.example.MAX_DELTA_X
import org.example.MAX_DELTA_Y
import org.example.MIN_DELTA_Y
import org.example.WIDTH
import org.example.generateRandomBall
import org.junit.jupiter.api.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BallTests {

    @Test
    fun testBallGeneration() {
        val testBall = generateRandomBall()

        assertTrue(testBall.x in 0..<WIDTH)
        assertTrue(testBall.y in 0..<HEIGHT)
        assertTrue(testBall.deltaX in 0..<MAX_DELTA_X)
        assertTrue(testBall.deltaY in -MAX_DELTA_Y..<-MIN_DELTA_Y)

        val testBall2 = generateRandomBall()

        assertFalse(testBall2.x !in 0..<WIDTH)
        assertFalse(testBall2.y !in 0..<HEIGHT)
        assertFalse(testBall2.deltaX !in 0..<MAX_DELTA_X)
        assertFalse(testBall2.deltaY !in -MAX_DELTA_Y..<-MIN_DELTA_Y)
    }
}

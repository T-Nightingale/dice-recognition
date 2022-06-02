import io.nightingale.dice.DiceDetection
import io.nightingale.dice.VideoDisplay
import nu.pattern.OpenCV
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.opencv.core.Core
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgcodecs.Imgcodecs.IMREAD_ANYCOLOR
import org.opencv.imgproc.Imgproc
import kotlin.test.assertFalse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DiceDetectionTest {
    @BeforeAll
    internal fun beforeAll() {
        OpenCV.loadShared()
    }

    @Test
    internal fun d6() {
        val resourcePath = resourcePath("d6.jpg")
        val d6Img = Imgcodecs.imread(resourcePath)

        assertFalse(d6Img.empty(), message = "Could not find file")

        val diceDetection = DiceDetection()
        val frame = diceDetection.processImage(d6Img)

        HighGui.imshow("d6", frame)
        HighGui.waitKey()
    }

    private fun resourcePath(fileName: String): String {
        return this.javaClass.classLoader.getResource(fileName)!!.path.replaceFirst("/", "")
    }


}
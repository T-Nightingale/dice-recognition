import io.nightingale.dice.DiceDetection
import io.nightingale.dice.VideoDisplay
import nu.pattern.OpenCV
import org.opencv.core.Mat
import org.opencv.videoio.VideoCapture

fun main(args: Array<String>) {
    OpenCV.loadShared()
    val camera = VideoCapture(0)
    val videoDisplay = VideoDisplay()
    val diceDetection = DiceDetection()


    while (true) {
        val frame = Mat()
        val read = camera.read(frame)
        if (!read) throw RuntimeException("Camera feed not found.")

        val processed = diceDetection.processImage(frame)
        videoDisplay.displayFrame(frame, processed)
    }
}
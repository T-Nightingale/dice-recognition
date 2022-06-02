package io.nightingale.dice

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

//https://pyimagesearch.com/2017/02/13/recognizing-digits-with-opencv-and-python/
class DiceDetection {

    fun processImage(frame: Mat): Mat {
        val preparedFrame = prepare(frame)
        
        // split image by dice
        val diceContours = diceContours(preparedFrame)

        // extract digits from dice

        // identify digits

        // draw for testing
        Imgproc.drawContours(preparedFrame, diceContours, -1, Scalar(1.0))
        return preparedFrame
    }

    private fun diceContours(processed: Mat): MutableList<MatOfPoint> {
        val edges = edges(processed)
        val contours = contours(edges)
        return approximateContours(contours);
    }

    private fun contours(edges: Mat): MutableList<MatOfPoint> {
        val contours = mutableListOf<MatOfPoint>()
        Imgproc.findContours(edges, contours, Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        return contours
    }

    private fun approximateContours(contours: MutableList<MatOfPoint>): MutableList<MatOfPoint> {
        val approxContours = mutableListOf<MatOfPoint>()
        for (contour in contours) {
            val contour2f = MatOfPoint2f()
            contour.convertTo(contour2f, CvType.CV_32F)
            val approxContour = MatOfPoint2f()
            // approximate the contour
            val arcLength = Imgproc.arcLength(contour2f, true)
            Imgproc.approxPolyDP(contour2f, approxContour, 0.02 * arcLength, true)

            contour.convertTo(contour, CvType.CV_32S)
            approxContours.add(contour)
        }
        return approxContours
    }

    private fun edges(processed: Mat): Mat {
        val edges = Mat(processed.size(), processed.type())
        //blur image to get rid of glare
        val kernelSize = 7.0
        Imgproc.GaussianBlur(processed, edges, Size(kernelSize, kernelSize), 0.0)

        //find edges
        Imgproc.Canny(edges, edges, 100.0, 10.0)

        return edges
    }

    private fun prepare(frame: Mat): Mat {
        val size = scaledSize(frame, 1000.0)
        val processed = Mat(size, frame.type())

        //shrink image
        Imgproc.resize(frame, processed, processed.size(), 0.0, 0.0, Imgproc.INTER_AREA)

        //grayscale image
        Imgproc.cvtColor(processed, processed, Imgproc.COLOR_RGB2GRAY)
        return processed
    }

    private fun scaledSize(frame: Mat, desiredHeight: Double): Size {
        val scale = desiredHeight / frame.height()
        val width = scale * frame.width()
        val height = scale * frame.height()
        return Size(width, height)
    }

}

package io.nightingale.dice

import org.opencv.core.Mat
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import javax.swing.JFrame
import javax.swing.JPanel

class VideoDisplay {
    private val cameraFeed = JPanel()
    private val processedFeed = JPanel()
    init {
        createJFrame(cameraFeed, processedFeed)
    }

    fun displayFrame(camera: Mat, processed: Mat) {
        drawImage(camera, cameraFeed)
        drawImage(processed, processedFeed)
    }

    private fun drawImage(mat: Mat, panel: JPanel) {
        val image = convertMatToBufferedImage(mat)

        // Draw image to panel
        val graphics = panel.graphics
        graphics.drawImage(image, 0, 0, panel)
    }

    private fun createJFrame(vararg panels: JPanel?) {
        val window = JFrame("Dice Detection")
        window.size = Dimension(panels.size * 640, 480)
        window.setLocationRelativeTo(null)
        window.isResizable = false
        window.layout = GridLayout(1, panels.size)
        for (panel in panels) {
            window.add(panel)
        }
        window.isVisible = true
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

    private fun convertMatToBufferedImage(mat: Mat): BufferedImage {
        // Create buffered image
        val bufferedImage = BufferedImage(
            mat.width(),
            mat.height(),
            if (mat.channels() == 1) BufferedImage.TYPE_BYTE_GRAY else BufferedImage.TYPE_3BYTE_BGR
        )

        // Write data to image
        val raster = bufferedImage.raster
        val dataBuffer = raster.dataBuffer as DataBufferByte
        mat[0, 0, dataBuffer.data]
        return bufferedImage
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.fxjme;

import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;



/**
 *
 * @author huliqing
 */
public class JmeView extends ImageView {

    private static final Logger LOG = Logger.getLogger(JmeView.class.getName());

    private WritableImage renderImage;
    private long frame;
    private long lastUpload;
    
    private int width;
    private int height;
    private int scanlineStride;
    
    public JmeView() {
        new AnimationTimer() {
            @Override
            public void handle(final long now) {
                frame++;
            }
        }.start();
        this.setScaleY(-1);
    }
    
    private synchronized void checkSize(int width, int height) {
        if (this.width != width || this.height != height) {
            scanlineStride = width * 3;
            renderImage = new WritableImage(width, height);
            setImage(renderImage);
            this.width = width;
            this.height = height;
        }
    }
    
    public void drawImage(ByteBuffer buffer, float w, float h) {
        if (!isVisible()) {
            return;
        }
        
        // Throttling, only update the JavaFX view once per frame.
        // *NOTE*: The +1 is weird here, but apparently setPixels triggers a new pulse within the current frame.
        // If we ignore that, we'd get a) worse performance from uploading double the frames and b) exceptions
        // on certain configurations (e.g. Nvidia GPU with the D3D pipeline).
        if (frame <= lastUpload + 1) {
            return;
        }
        lastUpload = frame;
        
        checkSize((int) w, (int) h);
        if (!buffer.hasRemaining()) {
            return;
        }
        
        PixelWriter pw = renderImage.getPixelWriter();
        pw.setPixels(0, 0, width, height, PixelFormat.getByteRgbInstance(), buffer, scanlineStride);
        
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxjme;

import com.jme3.renderer.RenderManager;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import name.huliqing.fxjme.JfxAppState.TransferRenderer;

/**
 *
 * @author huliqing
 */
public class JfxRenderer implements TransferRenderer{

    private static final Logger LOG = Logger.getLogger(JfxRenderer.class.getName());
    
    private boolean initialized;
    private final ByteBuffer byteBuffer;
    private final WritableImage renderImage;
    
    private  final int width;
    private  final int height;
    private  final int scanlineStride;
    
    private final ImageView imageView;
    
    private final FrameBuffer frameBuffer;

    public JfxRenderer(ImageView imageView, int width, int height, boolean depthBuffer) {
        this.imageView = imageView;
        this.width = width;
        this.height = height;
        this.byteBuffer = BufferUtils.createByteBuffer(width * height * 4);
        this.scanlineStride = width * 4;
        this.renderImage = new WritableImage(width, height);
        this.imageView.setImage(renderImage);
        if (depthBuffer) {
            frameBuffer = new FrameBuffer(width, height, 1);
            frameBuffer.setDepthBuffer(Format.Depth);
            frameBuffer.setColorBuffer(Format.BGRA8);
        } else {
            frameBuffer = null;
        }
    }
    
    @Override
    public void initialize(RenderManager rm) {
        if (frameBuffer != null) {
            rm.getRenderer().setMainFrameBufferOverride(frameBuffer);
        }
        rm.notifyReshape(width, height);
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void render(RenderManager rm, FrameBuffer out) {
        
        synchronized (byteBuffer) {
            rm.getRenderer().readFrameBufferWithFormat(out, byteBuffer, Format.BGRA8);
        }
        
        Platform.runLater(() -> {
            synchronized (byteBuffer) {
                PixelWriter pw = renderImage.getPixelWriter();
                pw.setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), byteBuffer, scanlineStride);
            }
        });

    }

    @Override
    public void cleanup() {
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }
        initialized = false;
    }
    
}

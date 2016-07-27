/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.fxjme;

import com.jme3.app.Application;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author huliqing
 */
public class JfxView extends ImageView implements EventHandler<MouseEvent>{

    private static final Logger LOG = Logger.getLogger(JfxView.class.getName());

    private Application application;
    
    private WritableImage renderImage;
    private long currentFrame;
    private long lastFrameUsed;
    
    private int width;
    private int height;
    private int scanlineStride;
    
    public JfxView() {
        new AnimationTimer() {
            @Override
            public void handle(final long now) {
                currentFrame++;
            }
        }.start();
        this.setScaleY(-1);
        
        addEventHandler(MouseEvent.ANY, this);
    }

    /**
     * Set the JME application
     * @param application 
     */
    void setApplication(Application application) {
        this.application = application;
    }

    /**
     * Get the JME application.
     * @return 
     */
    public Application getApplication() {
        return application;
    }
    
    @Override
    public void handle(MouseEvent event) {
        // 让Jfx View允许响应KeyEvent则必须让它可以获得焦点。
        switch (event.getButton()) {
            case PRIMARY:
                this.requestFocus();
                break;
            default :
                break;
        }
    }
    
    /**
     * 检查是否需要重设图片的大小
     * @param width
     * @param height 
     */
    private synchronized void checkResize(int width, int height) {
        if (this.width != width || this.height != height) {
            scanlineStride = width * 3;
            renderImage = new WritableImage(width, height);
            setImage(renderImage);
            this.width = width;
            this.height = height;
            LOG.log(Level.INFO, "resize jfxView={0}, {1}", new int[] {width, height});
        }
    }
    
    public void drawImage(ByteBuffer buffer, int w, int h) {
        if (!isVisible()) {
            return;
        }

        // 渲染涉率要尽量保持与JFX的频率一致，避免性能浪费。因为当JME端无限制FPS时，drawImage这个方法的调用频率与JME是
        // 一致的，可能达到成百上千，如果每次都重新渲染图片的话就会导致性能浪费，因为JFX的帧频率没有这么高.
        if (currentFrame <= lastFrameUsed) {
//            LOG.log(Level.INFO, "----donot render");
            return;
        }
//        LOG.log(Level.INFO, "++++render");
        lastFrameUsed = currentFrame;
        
        // 确定是否需要重置图片大小
        checkResize(w, h);
        
        if (!buffer.hasRemaining()) {
            return;
        }
        
        PixelWriter pw = renderImage.getPixelWriter();
        pw.setPixels(0, 0, width, height, PixelFormat.getByteRgbInstance(), buffer, scanlineStride);
        
    }

}

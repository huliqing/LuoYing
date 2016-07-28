/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxjme;

import com.jme3.app.Application;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import name.huliqing.fxjme.JfxAppState.RenderStore;

/**
 * 这个View用于支持将Jme的渲染结果显示到当前ImageView内。由于部分JmeContext(如：LwjglOffscreenBuffer)不能支持
 * restart,使用这些JmeContext进行渲染时不能动态改变JME窗口的大小，只能改变当前VIEW的大小。
 * @author huliqing
 */
public class JfxView extends ImageView implements EventHandler<MouseEvent>, ChangeListener<Number>{

    private static final Logger LOG = Logger.getLogger(JfxView.class.getName());

    private Application app;
    private JfxAppState jfxAppState;
    private RenderStore renderResult;
    
    private WritableImage renderImage;
    
    private int width;
    private int height;
    private int scanlineStride;
    
    private final IntegerProperty widthProperty = new SimpleIntegerProperty();
    private final IntegerProperty heightProperty = new SimpleIntegerProperty();
    
    private final AnimationTimer timer;
    
    public JfxView() {
        this.setScaleY(-1);
        
        // 添加一个鼠标事件监听，当鼠标点击到当前view时，将焦点定位到当前View,因为
        // 默认情况下ImageView是不会获得焦点的,需要特殊处理一下。
        addEventHandler(MouseEvent.ANY, this);
        
        // 使用与JFX动画线程同步的帧率就可以，不要浪费资源
        timer = new AnimationTimer() {
            @Override
            public void handle(final long now) {                
                render();
            }
        };
        
        widthProperty.addListener(this);
        heightProperty.addListener(this);
    }
    
    /**
     * Start jfxView renderer
     */
    public void start() {
        timer.start();
    }
    
    /**
     * Stop the jfxView renderer, this will also stop the jme application.
     */
    public void stop() {
        timer.stop();
        if (app != null) {
            app.stop();
        }
    }
    
    protected void render() {
        if (!isVisible()) {
            return;
        }
        
        if (jfxAppState == null) {
            if (app != null) {
                jfxAppState = app.getStateManager().getState(JfxAppState.class);
            }
            return;
        }

        // 确定是否需要重置图片大小
        renderResult = jfxAppState.renderResult;
        if (renderResult == null) {
            return;
        }
        if (!renderResult.buffer.hasRemaining()) {
            return;
        }
        
        // 检查并重置大小
        checkResize(renderResult.width, renderResult.height);
        
        PixelWriter pw = renderImage.getPixelWriter();
        pw.setPixels(0, 0, width, height, PixelFormat.getByteRgbInstance(), renderResult.buffer, scanlineStride);
    }

    /**
     * Get the JME application.
     * @return 
     */
    public Application getApplication() {
        return app;
    }

    /**
     * Set the JME application
     * @param app 
     */
    public void setApplication(Application app) {
        this.app = app;
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
    private void checkResize(int width, int height) {
        if (this.width != width || this.height != height) {
            scanlineStride = width * 3;
            renderImage = new WritableImage(width, height);
            setImage(renderImage);
            this.width = width;
            this.height = height;
            setFitWidth(width);
            setFitHeight(height);
            LOG.log(Level.INFO, "resize jfxView={0}, {1}", new int[] {width, height});
        }
    }

    public final IntegerProperty widthProperty() {
        return widthProperty;
    }
    
    public final IntegerProperty heightProperty() {
        return heightProperty;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        LOG.log(Level.INFO, "value change: observable={0}, oldValue={1}, newValue={2}", new Object[] {observable, oldValue, newValue});
        final int w = widthProperty.getValue();
        final int h = heightProperty.getValue();
        app.enqueue(() -> {
            if (jfxAppState != null) {
                jfxAppState.setResolution(w, h);
            }
        });
        
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxjme;

import com.jme3.app.Application;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * 这个View用于支持将Jme的渲染结果显示到当前ImageView内。由于部分JmeContext(如：LwjglOffscreenBuffer)不能支持
 * restart,使用这些JmeContext进行渲染时不能动态改变JME窗口的大小，只能改变当前VIEW的大小。
 * @author huliqing
 */
public class JfxView extends ImageView implements EventHandler<MouseEvent>, ChangeListener<Number>{

    private static final Logger LOG = Logger.getLogger(JfxView.class.getName());

    private Application app;
    private final JfxAppState jfxAppState;
    private boolean useDepthBuffer;
    
    private int width;
    private int height;
    
    public JfxView(JfxAppState jfxAppState, int width, int height) {
        this.jfxAppState = jfxAppState;
        
        this.fitWidthProperty().addListener(this);
        this.fitHeightProperty().addListener(this);
        
        // 添加一个鼠标事件监听，当鼠标点击到当前view时，将焦点定位到当前View,因为
        // 默认情况下ImageView是不会获得焦点的,需要特殊处理一下。
        addEventHandler(MouseEvent.ANY, this);
        
        setResolution(width, height);
    }
    
    public void setUseDepthBuffer(boolean useDepthBuffer) {
        this.useDepthBuffer = useDepthBuffer;
        setResolution(this.width, this.height);
    }
    
    /**
     * Stop the jfxView renderer, this will also stop the jme application.
     */
    public void stop() {
        if (app != null) {
            app.stop();
        }
    }
    
    public final void setResolution(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        LOG.log(Level.INFO, "setResolution, width={0}, height={1}", new Object[] {width, height});
        this.width = width;
        this.height = height;
        jfxAppState.setTransferRenderer(new JfxRenderer(this, width, height, useDepthBuffer));
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

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        final int w = this.fitWidthProperty().getValue().intValue();
        final int h = this.fitHeightProperty().getValue().intValue();
        setResolution(w, h);
    }


}

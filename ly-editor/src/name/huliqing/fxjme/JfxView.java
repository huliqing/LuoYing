/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.fxjme;

import com.jme3.app.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * 这个View用于支持将Jme的渲染结果显示到当前ImageView内。由于部分JmeContext(如：LwjglOffscreenBuffer)不能支持
 * restart,使用这些JmeContext进行渲染时不能动态改变JME窗口的大小，只能改变当前VIEW的大小。
 * @author huliqing
 */
public class JfxView extends ImageView implements EventHandler<MouseEvent>, ChangeListener<Number>{

//    private static final Logger LOG = Logger.getLogger(JfxView.class.getName());

    private final Application app;
    private final JfxAppState jfxAppState;
    private boolean useDepthBuffer;
    
    private int keepResolutionMaxWidth;
    private int keepResolutionMaxHeight;
    
    private JfxMouseInput mouseInput;
    private JfxKeyInput keyInput;
    
    public JfxView(Application app, JfxAppState jfxAppState, int width, int height) {
        this.app = app;
        this.jfxAppState = jfxAppState;
        
        // 添加一个鼠标事件监听，当鼠标点击到当前view时，将焦点定位到当前View,因为
        // 默认情况下ImageView是不会获得焦点的,需要特殊处理一下。
        addEventHandler(MouseEvent.ANY, this);
        
        this.fitWidthProperty().addListener(this);
        this.fitHeightProperty().addListener(this);
        this.fitWidthProperty().setValue(width);
        this.fitHeightProperty().setValue(height);
    }
    
    /**
     * 保持分辨率的比较在一定范围内，这可以使分辨率不会随着窗口的放大而放大，这可以提高一些性能。该方法会把3D场景的
     * 最高分辨率限制在maxWidth和maxHeight之间。
     * @param maxWidth
     * @param maxHeight 
     */
    public void setResolutionLimit(int maxWidth, int maxHeight) {
        this.keepResolutionMaxWidth = maxWidth;
        this.keepResolutionMaxHeight = maxHeight;
    }
    
    public void setUseDepthBuffer(boolean useDepthBuffer) {
        this.useDepthBuffer = useDepthBuffer;
        resetRenderer();
    }
    
    /**
     * Set enable or disable mouse event convert from Jfx to Jme
     * @param enabled 
     */
    public void setMouseEventEnabled(boolean enabled) {
        if (enabled) {
            if (mouseInput == null) {
                JfxContext jfxContext = (JfxContext) app.getContext();
                mouseInput = (JfxMouseInput) jfxContext.getMouseInput();
                addEventHandler(Event.ANY, mouseInput);
            }
        } else {
            if (mouseInput != null) {
                removeEventHandler(Event.ANY, mouseInput);
            }
        }
    }
    
    /**
     * Set enable or disable key event convert from Jfx to Jme
     * @param enabled 
     */
    public void setKeyEventEnabled(boolean enabled) {
        if (enabled) {
            if (keyInput == null) {
                JfxContext jfxContext = (JfxContext) app.getContext();
                keyInput = (JfxKeyInput) jfxContext.getKeyInput();
                addEventHandler(KeyEvent.ANY, keyInput);
            }
        } else {
            if (keyInput != null) {
                removeEventHandler(KeyEvent.ANY, keyInput);
            }
        }
    }
    
    /**
     * Stop the jfxView renderer, this will also stop the jme application.
     */
    public void stop() {
        if (app != null) {
            app.stop();
        }
    }
    
    private void resetRenderer() {
        if (fitWidthProperty().intValue() <= 0 || fitHeightProperty().intValue()<= 0) {
            return;
        }
        int width = fitWidthProperty().intValue();
        int height = fitHeightProperty().intValue();
        
        if (keepResolutionMaxWidth > 0 || keepResolutionMaxHeight > 0) {
            float rate = (float) width / height;
            
            if (keepResolutionMaxWidth > 0 && width > keepResolutionMaxWidth) {
                width = keepResolutionMaxWidth;
                height = (int) (width / rate);
                rate = (float) width / height;
            }
            
            if (keepResolutionMaxHeight > 0 && height > keepResolutionMaxHeight) {
                height = keepResolutionMaxHeight;
                width = (int) (rate * height);
            }
            
        }
        
//        LOG.log(Level.INFO, "resetRenderer, width={0}, height={1}", new Object[] {width, height});
        jfxAppState.setTransferRenderer(new JfxRenderer(this, width, height, useDepthBuffer));
        app.getContext().getSettings().setResolution(width, height);
    }
    
    /**
     * Get the JME application.
     * @return 
     */
    public Application getApplication() {
        return app;
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
        resetRenderer();
    }


}

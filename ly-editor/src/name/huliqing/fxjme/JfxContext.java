/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxjme;

import com.jme3.input.JoyInput;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.renderer.Renderer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.jme3.system.SystemListener;
import com.jme3.system.Timer;

/**
 *
 * @author huliqing
 */
public class JfxContext implements JmeContext {
    
    /**
     * JfxContext wrap renderer key.
     */
    public final static String JFX_WRAP_RENDERER = "jfxWrapRenderer";

    // Convert event from JFX to JME
    private final JfxMouseInput jfxMouseInput = new JfxMouseInput();
    private final JfxKeyInput jfxKeyInput = new JfxKeyInput();
    
    private AppSettings settings = new AppSettings(true);
    private JmeContext innerContext;
    private SystemListener listener;
    
    @Override
    public Type getType() {
        return Type.OffscreenSurface;
    }

    @Override
    public void setSettings(AppSettings settings) {
        this.settings = settings;
        
        // 实际的渲染引擎，如果进行了特别指定,否则使用LWJGL_OPENGL2作为实际的内部包装用擎
        String jfxRenderer = (String) this.settings.get(JFX_WRAP_RENDERER);
        if (jfxRenderer != null) {
            this.settings.setRenderer(jfxRenderer);
        } else {
            this.settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        }
        
        if (innerContext != null){
            innerContext.setSettings(this.settings);
        }
    }

    @Override
    public void setSystemListener(SystemListener listener) {
        this.listener = listener;
        if (innerContext != null) {
            innerContext.setSystemListener(listener);
        }
    }

    @Override
    public AppSettings getSettings() {
        return settings;
    }

    @Override
    public Renderer getRenderer() {
        return innerContext.getRenderer();
    }

    @Override
    public MouseInput getMouseInput() {
        return jfxMouseInput;
    }

    @Override
    public KeyInput getKeyInput() {
        return jfxKeyInput;
    }

    @Override
    public JoyInput getJoyInput() {
        return innerContext.getJoyInput();
    }

    @Override
    public TouchInput getTouchInput() {
        return innerContext.getTouchInput();
    }

    @Override
    public Timer getTimer() {
        return innerContext.getTimer();
    }

    @Override
    public void setTitle(String title) {
        innerContext.setTitle(title);
    }

    @Override
    public boolean isCreated() {
        return innerContext != null && innerContext.isCreated();
    }

    @Override
    public boolean isRenderable() {
        return innerContext != null && innerContext.isRenderable();
    }

    @Override
    public void setAutoFlushFrames(boolean enabled) {
        if (innerContext != null) {
            innerContext.setAutoFlushFrames(enabled);
        }
    }

    @Override
    public void create(boolean waitFor) {
        if (innerContext != null){
            throw new IllegalStateException("Already created");
        }
        innerContext = JmeSystem.newContext(settings, getType());
        innerContext.setSystemListener(listener);
        innerContext.create(waitFor);
    }

    @Override
    public void restart() {
        if (innerContext != null) {
            innerContext.restart();
        }
    }

    @Override
    public void destroy(boolean waitFor) {
        if (innerContext != null) {
            innerContext.destroy(waitFor);
        }
    }

    
}

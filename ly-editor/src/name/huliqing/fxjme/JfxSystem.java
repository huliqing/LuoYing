/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fxjme;

import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.scene.input.KeyEvent;

/**
 * 
 * @author huliqing
 */
public class JfxSystem {

    private static final Logger LOG = Logger.getLogger(JfxSystem.class.getName());
    
    /**
     * Create JfxView and start application.
     * @param appClass
     * @param width the app'with, also is the JfxView's width
     * @param height the app'height, also is the JfxView's height
     * @return 
     * @see #startApp(java.lang.String, com.jme3.system.AppSettings) 
     */
    public static JfxView startApp(String appClass, int width, int height) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(width, height);
        settings.setFrameRate(60);
        return startApp(appClass, settings);
    }
    
    /**
     * Create JfxView and start application. the renderer will be set to custom, see below:<BR>
     * settings.setRenderer("CUSTOM" + JfxContext.class.getName())) , this custom renderer is a proxy.<BR>
     * If you need to specify the actual renderer, you need to set like below:<BR>
     * settings.put(JfxContext.JFX_WRAP_RENDERER, AppSettings.LWJGL_OPENGL3);
     * 
     * @param appClass
     * @param settings
     * @return 
     */
    public static JfxView startApp(String appClass,  AppSettings settings) {
        // ---- Custom JmeContext for render.
        settings.setCustomRenderer(JfxContext.class);
        
        // Specify a true renderer to render game, default is: LWJGL_OPENGL2
//        settings.put(JfxContext.JFX_WRAP_RENDERER, AppSettings.LWJGL_OPENGL3);

        // ---- Create Application
        LegacyApplication app;
        try{
            Class<? extends LegacyApplication> clazz = (Class<? extends LegacyApplication>) Class.forName(appClass);
            app = clazz.newInstance();
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex){
            LOG.log(Level.SEVERE, "Could not create application by appClass={0}, error={1}"
                    , new Object[]{appClass, ex.getMessage()});
            return null;
        }
        
        JfxAppState jfxAppState = new JfxAppState();
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.getStateManager().attach(jfxAppState);
        app.start(JmeContext.Type.OffscreenSurface);
        
        // ---- Create JfxView and set event converter
        
        JfxView jfxView = new JfxView(app, jfxAppState, settings.getWidth(), settings.getHeight());
        jfxView.setSmooth(true);
        jfxView.setCache(true);
        jfxView.setScaleY(-1);
        jfxView.setPreserveRatio(true);
        jfxView.setMouseEventEnabled(true);
        jfxView.setKeyEventEnabled(true);
        return jfxView;
        
    }
}

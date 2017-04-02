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

import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import java.util.logging.Level;
import java.util.logging.Logger;

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

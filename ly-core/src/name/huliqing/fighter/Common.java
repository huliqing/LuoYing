/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.InputManager;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import name.huliqing.fighter.game.state.PlayState;

/**
 *
 * @author huliqing
 */
public class Common {
    
    private static SimpleApplication app;
    private static AppSettings settings; 
    private static BitmapFont font; 
    
    public static void setCommon(SimpleApplication app, AppSettings settings) {
        Common.app = app;
        Common.settings = settings;
    }
    
    public static SimpleApplication getApp() {
        return app;
    }

    public static AssetManager getAssetManager() {
        if (app == null) {
            DesktopAssetManager am = new DesktopAssetManager(Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));
            return am;
        }
        
        return app.getAssetManager();
    }
    
    public static RenderManager getRenderManager() {
        return app.getRenderManager();
    }
    
    /**
     * 获取当前的ViewPort
     * @return 
     */
    public static ViewPort getViewPort() {
        return app.getViewPort();
    }
    
    public static InputManager getInputManager() {
        return app.getInputManager();
    }
    
    public static AppStateManager getStateManager() {
        return app.getStateManager();
    }

    public static AppSettings getSettings() {
        return settings;
    }
    
    public static BitmapFont getFont() {
        if (font == null) {
            font = getAssetManager().loadFont("data/font/chinese.fnt");
        }
        return font;
    }
    
    public static float getFontSize() {
        float size = getFont().getCharSet().getRenderedSize(); // 32
        return size;
    }
    
    public static PlayState getPlayState() {
        return app.getStateManager().getState(PlayState.class);
    }
    
    /**
     * 获取当前光标位置
     * @return 
     */
    public static Vector2f getCursorPosition() {
        return app.getInputManager().getCursorPosition();
    }
    
    /**
     * @deprecated 
     * 获取当前游戏时间,返回游戏运行到当前时间的毫秒数
     * @return 
     */
    public static long getGameTime() {
        return System.currentTimeMillis();
    }
    
    public static long getGameNanoTime() {
        return System.nanoTime();
    }
    
    public static void preloadScene(Spatial spatial) {
        app.getRenderManager().preloadScene(spatial);
    }
}

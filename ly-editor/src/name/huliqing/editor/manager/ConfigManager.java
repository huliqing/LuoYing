/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;

/**
 *
 * @author huliqing
 */
public class ConfigManager {
    
    private final static ResManager RES_MANAGER = new ResManager();
    private static BitmapFont font;
    public final static int FONT_SIZE = 18;
    
    public static void initialize(Application app) {
        font = app.getAssetManager().loadFont("/resources/font/chinese.fnt");
        
        RES_MANAGER.clearResources();
        RES_MANAGER.loadResource("/resources/resource_en_US", "utf-8", "en_US");
        RES_MANAGER.loadResource("/resources/resource_zh_CN", "utf-8", "zh_CN");
        RES_MANAGER.setLocale("zh_CN");
        RES_MANAGER.setLocaleDefault("en_US");
    }
    
    public final static BitmapFont getFont() {
        return font;
    }
    
    public final static ResManager getResManager() {
        return RES_MANAGER;
    }
    
    public final static String getRes(String resKey) {
        return RES_MANAGER.get(resKey);
    }
}

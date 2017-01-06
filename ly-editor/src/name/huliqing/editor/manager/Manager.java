/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;

/**
 * 全局管理器
 * @author huliqing
 */
public class Manager {
    
    private static BitmapFont font;
    public final static int FONT_SIZE = 18;
    
    private final static ResManager RES_MANAGER = new ResManager();
    private static boolean resLoaded;
    
    private final static ConfigManager CONFIG_MANAGER = new ConfigManager();
    private static boolean configLoaded;
    
    public static void initialize(Application app) {
        // 载入资源文件
        initializeResource();

        // 载入配置
        initializeConfig();

        // 载入字体
        font = app.getAssetManager().loadFont("/resources/font/chinese.fnt");
        
        app.getAssetManager().registerLocator("", EditAssetLocator.class);
    }
    
    // 资源文件必须优先载入,因为JFX也要依赖这个资源
    private static void initializeResource() {
        if (resLoaded) {
            return;
        }
        RES_MANAGER.clearResources();
        RES_MANAGER.loadResource("/resources/resource_en_US", "utf-8", "en_US");
        RES_MANAGER.loadResource("/resources/resource_zh_CN", "utf-8", "zh_CN");
        RES_MANAGER.setLocale("zh_CN");
        RES_MANAGER.setLocaleDefault("en_US");
        resLoaded = true;
    }
    
    private static void initializeConfig() {
        if (configLoaded) {
            return;
        }
        CONFIG_MANAGER.loadConfig();
        configLoaded = true;
    }
    
    public static void saveOnQuick() {
        CONFIG_MANAGER.saveConfig();
    }
    
    public final static BitmapFont getFont() {
        return font;
    }
    
    public final static ConfigManager getConfigManager() {
        if (!configLoaded) {
            initializeConfig();
        }
        return CONFIG_MANAGER;
    }
    
    public final static ResManager getResManager() {
        if (!resLoaded) {
            initializeResource();
        }
        return RES_MANAGER;
    }
    
    /**
     * 获取资源
     * @param resKey
     * @return 
     */
    public final static String getRes(String resKey) {
        return getResManager().get(resKey);
    }
    public final static String getRes(String resKey, Object[] params) {
        return getResManager().get(resKey, params);
    }
    
}

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
package name.huliqing.editor.manager;

import name.huliqing.editor.EditAssetLocator;
import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import name.huliqing.editor.log.LogFactory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.TextFileLoader;
import name.huliqing.editor.UncacheAssetEventListener;

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
        // 资源路径，及资源载入器实始化
        app.getAssetManager().registerLoader(TextFileLoader.class, "ini", "xml");
        app.getAssetManager().registerLocator("", EditAssetLocator.class);
        app.getAssetManager().addAssetEventListener(UncacheAssetEventListener.getInstance());
        
        // 日志初始化
        LogFactory.initialize();
        
        // 载入编辑器资源文件
        initializeResource();

        // 载入配置
        initializeConfig();

        // 载入字体
        font = app.getAssetManager().loadFont("/resources/font/chinese.fnt");

        // 落樱初始化,需要先初始化
        LuoYing.initialize(app);
        
        // 重新设置一次资源文件夹，以便触发资源的重新载入。
        String mainAssetDir = CONFIG_MANAGER.getMainAssetDir();
        if (mainAssetDir != null && !mainAssetDir.isEmpty()) {
            CONFIG_MANAGER.setMainAssetDir(mainAssetDir);
        } 
        
    }
    
    public static void cleanup() {
        CONFIG_MANAGER.saveConfig();
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

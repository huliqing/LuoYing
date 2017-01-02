/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置管理器
 * @author huliqing
 */
public class ConfigManager {
    
    private final ConfigSaver cs = new ConfigSaver();
    private final String configDir = new File("config").getAbsolutePath();
    private final String configFile = "config.ini";
    
    public interface ConfigChangedListener {
        
        /**
         * 当指定配置发生变化时该方法被调用。
         * @param key
         */
        void onConfigChanged(String key);
    }
    
    private final List<ConfigChangedListener> listeners = new ArrayList<ConfigChangedListener>();
    
    /** 所有使用过的资源文件路径 */
    public final static String KEY_ASSETS = "assets";
    
    /** 主资源文件夹 */
    public final static String KEY_MAIN_ASSETS = "mainAssets";
    
    private File getDir() {
        File file = new File(configDir);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }
    
    public void loadConfig() {
        File file = new File(getDir(), configFile);
        if (file.exists() && file.isFile()) {
            cs.load(file);
        }
    }
    
    public void saveConfig() {
        File file = new File(getDir(), configFile);
        cs.save(file);
    }
    
    public void addListener(ConfigChangedListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public boolean removeListener(ConfigChangedListener listener) {
        return listeners.remove(listener);
    }
    
    private void notifyListeners(String key) {
        listeners.stream().forEach(t -> {
            t.onConfigChanged(key);
        });
    }
    
    private void addAssetsDirs(String assetsDir) {
        if (assetsDir == null || assetsDir.trim().isEmpty()) 
            return;
        List<String> assetsDirs = cs.getAsList(KEY_ASSETS);
        if (assetsDirs == null) {
            assetsDirs = new ArrayList<String>();
        }
        if (!assetsDirs.contains(assetsDir)) {
            assetsDirs.add(assetsDir);
        }
        cs.set(KEY_ASSETS, assetsDirs);
        notifyListeners(KEY_ASSETS);
    }
    
    /**
     * 获取最近使用过的所有资源文件夹的路径.
     * @return 
     */
    public List<String> getAssetsDirs() {
        return cs.getAsList(KEY_ASSETS);
    }
    
    public void setAssetsDirs(List<String> assetsDirs) {
        cs.set(KEY_ASSETS, assetsDirs);
        notifyListeners(KEY_ASSETS);
    }
    
    /**
     * 获取最近一个刚刚使用的资源文件夹路径,绝对文件路径
     * @return 
     */
    public String getMainAssetDir() {
        return cs.get(KEY_MAIN_ASSETS);
    }
    
    /**
     * 设置最近一个使用的资源文件夹的路径，绝对文件路径
     * @param assetsDir 
     */
    public void setMainAssetDir(String assetsDir) {
        cs.set(KEY_MAIN_ASSETS, assetsDir);
        addAssetsDirs(assetsDir);
        notifyListeners(KEY_MAIN_ASSETS);
    }
    

}

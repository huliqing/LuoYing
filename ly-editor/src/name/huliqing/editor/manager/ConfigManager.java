/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.constants.ConfigConstants;
import name.huliqing.luoying.LuoYing;
import org.apache.commons.io.FileUtils;

/**
 * 配置管理器
 * @author huliqing
 */
public class ConfigManager {

    private static final Logger LOG = Logger.getLogger(ConfigManager.class.getName());
    
    /**
     * 模板复制的目标资源文件夹名称，这个根目标在Assets的根目录下，复制后目录结构类似：
     * <code>
     * <pre>
     * assets/LuoYing/assets
     * assets/LuoYing/data
     * assets/LuoYing/editor
     * assets/LuoYing/...
     * </pre>
     * </code>
     */
    private final static String LUO_YING_DIR = "LuoYing";
    
    /**
     * “组件定义”目录的根路径( in assets), 该根目录下的组件定义文件必须是以".xml"为后缀结尾的组件定义文件。
     */
    private final static String LUO_YING_COMPONENT_DIR = "LuoYing/editor/component";
    
    /**
     * "转换器定义" 目录的根路径(in assets),该根目录下的转换器定义文件必须是以".xml"为后缀结尾的组件定义文件。
     */
    private final static String LUO_YING_CONVERTER_DIR = "LuoYing/editor/converter";
    
    private final ConfigSaver configs = new ConfigSaver();
    
    public interface ConfigChangedListener {
        
        /**
         * 当指定配置发生变化时该方法被调用。
         * @param key
         */
        void onConfigChanged(String key);
    }
    
    private final List<ConfigChangedListener> listeners = new ArrayList<ConfigChangedListener>();
    
    public void loadConfig() {
        File file = new File(ConfigConstants.CONFIG_PATH);
        configs.load(file);
    }
    
    public void saveConfig() {
        File file = new File(ConfigConstants.CONFIG_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        configs.save(file);
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
        List<String> assetsDirs = configs.getAsList(ConfigConstants.KEY_ASSETS);
        if (assetsDirs == null) {
            assetsDirs = new ArrayList<String>();
        }
        if (!assetsDirs.contains(assetsDir)) {
            assetsDirs.add(assetsDir);
        }
        configs.set(ConfigConstants.KEY_ASSETS, assetsDirs);
        notifyListeners(ConfigConstants.KEY_ASSETS);
    }
    
    /**
     * 获取最近使用过的所有资源文件夹的路径.
     * @return 
     */
    public List<String> getAssetsDirs() {
        return configs.getAsList(ConfigConstants.KEY_ASSETS);
    }
    
    public void setAssetsDirs(List<String> assetsDirs) {
        configs.set(ConfigConstants.KEY_ASSETS, assetsDirs);
        notifyListeners(ConfigConstants.KEY_ASSETS);
    }
    
    /**
     * 获取最近一个刚刚使用的资源文件夹路径,绝对文件路径
     * @return 
     */
    public String getMainAssetDir() {
        return configs.get(ConfigConstants.KEY_MAIN_ASSETS);
    }
    
    /**
     * 设置主资源文件夹的路径，绝对文件路径
     * @param assetsDir 
     */
    public void setMainAssetDir(String assetsDir) {
        configs.set(ConfigConstants.KEY_MAIN_ASSETS, assetsDir);
        // 将资源路径添加到资源列表中
        addAssetsDirs(assetsDir);
        
        // 1.检查并复制模板配置文件到资源目录下.
        copyTemplate(assetsDir);
        // 2.切换资源后需要重新载入落樱资源
        LuoYing.reloadData();
        // 3.切换资源文件夹后必须重新载入组件定义及转换器定义
        ComponentManager.reloadComponents(new File(assetsDir, LUO_YING_COMPONENT_DIR));
        ConverterManager.reloadConverters(new File(assetsDir, LUO_YING_CONVERTER_DIR));
        
        // 通知监听器
        notifyListeners(ConfigConstants.KEY_MAIN_ASSETS);
    }
    
    private void copyTemplate(String assetsDir) {
        File assetsRootDir = new File(assetsDir);
        // 资源夹不存在
        if (!assetsRootDir.exists() || !assetsRootDir.isDirectory()) 
            return;
        
        // 注意：必须检查资源是否已经存在，不要去覆盖这些文件。
        // 要重新生成这些文件时，可以由用户先手动删除指定的文件后再重新生成。
        File luoYingDir = new File(assetsRootDir, LUO_YING_DIR);
        if (luoYingDir.exists() && luoYingDir.isDirectory()) {
            return;
        }
        
        String templateDirPath = configs.get(ConfigConstants.KEY_TEMPLATE);
        File luoYingTemplateDir = new File(templateDirPath);
        if (!luoYingTemplateDir.exists()) {
            LOG.log(Level.SEVERE, "LuoYing template directory not found, path={0}", templateDirPath);
            return;
        }
        
        if (!luoYingTemplateDir.isDirectory()) {
            LOG.log(Level.SEVERE, "LuoYing template not a directory, path={0}", templateDirPath);
            return;
        }
        
        try {
            FileUtils.copyDirectory(luoYingTemplateDir, luoYingDir);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not copy template file to assets directory! assetsDir=" + assetsDir, ex);
        }
    }
}

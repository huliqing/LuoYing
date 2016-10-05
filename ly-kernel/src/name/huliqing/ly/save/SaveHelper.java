/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.save;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.SaveService;

/**
 *
 * @author huliqing
 */
public class SaveHelper {
    private static final Logger LOG = Logger.getLogger(SaveHelper.class.getName());
    
    // 全局配置的键值
    private final static String KEY_CONFIG_GLOBAL = "ly3d_config";
    // 存档列表
    private final static String KEY_SAVE_LIST = "ly3d_saveList";
    // 最近一次自动存档
    private final static String KEY_SAVE_LAST = "SaveLast";
    // 存档名称格式
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
    
    /**
     * 获取存档列表
     * @return 
     */
    public static SaveStoryList loadStoryList() {
        try {
            SaveService saveService = Factory.get(SaveService.class);
            SaveStoryList sl = (SaveStoryList) saveService.loadSavable(KEY_SAVE_LIST);
            return sl;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not loadStoryList, e={0}", e);
        }
        return null;
    }
    
    /**
     * 最近一次自动存档
     * @param ss 
     */
    public static void saveStoryLast(SaveStory ss) {
        try {
            ss.setSaveName(KEY_SAVE_LAST);
            saveStory(ss);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not saveStoryLast, e={0}", e);
        }
    }
    
    /**
     * 将最近一次存档保存为新存档
     */
    public static void saveStoryNew() {
        try {
            SaveService saveService = Factory.get(SaveService.class);
            SaveStory newSave = (SaveStory) saveService.loadSavable(KEY_SAVE_LAST);
            if (newSave != null) {
                newSave.setSaveName("Save" + sdf.format(new Date()));
                saveStory(newSave);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not saveStoryNew, e={0}", e);
        }
    }
    
    public static SaveStory loadStory(String key) {
        try {
            SaveService saveService = Factory.get(SaveService.class);
            SaveStory saveStory = (SaveStory) saveService.loadSavable(key);
            return saveStory;
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "Could not loadStory, e={0}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static SaveStory loadStoryLast() {
        try {
            return loadStory(KEY_SAVE_LAST);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not loadStoryLast, e={0}", e);
        }
        return null;
    }

    public static void deleteStory(String key) {
        try {
            // 删除存档
            SaveService saveService = Factory.get(SaveService.class);
            saveService.delete(key);

            // 保存存档列表
            SaveStoryList list = loadStoryList();
            list.removeSaveName(key);
            saveStoryList(list);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not deleteStory, e={0}", e);
        }
    }
    
    /**
     * 判断是否存在最近一次存档
     * @return 
     */
    public static boolean existsLastSaveStory() {
        SaveService saveService = Factory.get(SaveService.class);
        return saveService.existsSaveKey(KEY_SAVE_LAST);
    }
    
    /**
     * 保存全局配置
     * @param saveConfig 
     */
    public static void saveConfig(SaveConfig saveConfig) {
        try {
            SaveService saveService = Factory.get(SaveService.class);
            saveService.saveSavable(KEY_CONFIG_GLOBAL, saveConfig);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not saveConfig, e={0}", e);
        }
    }
    
    /**
     * 载入全局配置
     * @return 
     */
    public static SaveConfig loadConfig() {
        try {
            SaveService ss = Factory.get(SaveService.class);
            SaveConfig sc = (SaveConfig) ss.loadSavable(KEY_CONFIG_GLOBAL);
            return sc;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not loadStoryLast, e={0}", e);
        }
        return null;
    }
    
    // --------------------
    
    private static void saveStory(SaveStory saveStory) {
        // 保存存档
        SaveService saveService = Factory.get(SaveService.class);
        saveService.saveSavable(saveStory.getSaveName(), saveStory);
        
        // 保存存档列表
        SaveStoryList list = loadStoryList();
        if (list == null) {
            list = new SaveStoryList();
        }
        list.addSaveName(saveStory.getSaveName());
        saveStoryList(list);
    }
    
    /**
     * 保存存档列表
     * @param sl 
     */
    private static void saveStoryList(SaveStoryList sl) {
        SaveService ss = Factory.get(SaveService.class);
        ss.saveSavable(KEY_SAVE_LIST, sl);
    }
    
}

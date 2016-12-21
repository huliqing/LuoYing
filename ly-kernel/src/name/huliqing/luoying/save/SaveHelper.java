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
package name.huliqing.luoying.save;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.SaveService;

/**
 *
 * @author huliqing
 */
public class SaveHelper {
    private static final Logger LOG = Logger.getLogger(SaveHelper.class.getName());
    
    // 存档列表
    private final static String KEY_SAVE_LIST = "SaveList";
    // 最近一次自动存档
    private final static String KEY_SAVE_LAST = "SaveLast";
    // 存档名称格式
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
    
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
            LOG.log(Level.SEVERE, "Could not loadStoryList, error=" + e.getMessage(), e);
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
            LOG.log(Level.SEVERE, "Could not saveStoryLast, error=" + e.getMessage(), e);
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
                newSave.setSaveName("Save" + FORMAT.format(new Date()));
                saveStory(newSave);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not saveStoryNew, error=" + e.getMessage(), e);
        }
    }
    
    public static SaveStory loadStory(String key) {
        try {
            SaveService saveService = Factory.get(SaveService.class);
            SaveStory saveStory = (SaveStory) saveService.loadSavable(key);
            return saveStory;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not loadStory, error=" + e.getMessage(), e);
        }
        return null;
    }
    
    public static SaveStory loadStoryLast() {
        try {
            return loadStory(KEY_SAVE_LAST);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not loadStoryLast, error=" + e.getMessage(), e);
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
            LOG.log(Level.SEVERE, "Could not deleteStory, error=" + e.getMessage(), e);
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

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
package name.huliqing.ly.manager;

import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 资源文件读取类
 * @author huliqing
 */
public class ResourceManager {
    
    /**
     * 从默认资源文件中获取资源
     * @param key
     * @return 
     */
    public static String get(String key) {
        return ResManager.get(key);
    }
    
    /**
     * 从默认资源文件中获取资源
     * @param key
     * @param params
     * @return 
     */
    public static String get(String key, Object[] params) {
        return ResManager.get(key, params);
    }
    
    /**
     * 获得物体信息（扩展）
     * @param objectId 物品ID名称
     * @param ext
     * @return 
     */
    public static String getObjectExt(String objectId, String ext) {
        return get(objectId + "." + ext);
    }
    
    /**
     * 判断是否存在objectName的定义
     * @param objectId
     * @return 
     */
    public static boolean existsObjectName(String objectId) {
        return !(getObjectName(objectId).startsWith("<"));
    }
    
    /**
     * 获取物品的名称
     * @param data
     * @return 
     */
    public static String getObjectName(ObjectData data) {
        return getObjectName(data.getId());
    }
    
    /**
     * 通过物品的ID来获得物品的名称
     * @param objectId
     * @return 
     */
    public static String getObjectName(String objectId) {
        return getObjectExt(objectId, "name");
    }
    
    public static String getObjectDes(String objectId) {
        return getObjectExt(objectId, "des");
    }
    
    /**
     * 获取任务的开始对话 
     * @param taskId 任务ID
     * @return 
     */
    public static String[] getTaskChatStart(String taskId) {
        return getObjectExt(taskId, "chatStart").split("\\|\\|");
    }
    
    /**
     * 获取任务的“询问”对话，即询问任务完成了没有.
     * @param taskId 任务ID
     * @return 
     */
    public static String[] getTaskChatAsk(String taskId) {
        return getObjectExt(taskId, "chatAsk").split("\\|\\|");
    }
    
    /**
     * 获取任务的结束对话。
     * @param taskId 任务ID
     * @return 
     */
    public static String[] getTaskChatEnd(String taskId) {
        return getObjectExt(taskId, "chatEnd").split("\\|\\|");
    }
    
    /**
     * 获取字符串的单词长度，对于中文，每一个字作为1个长度，对于英文，每一个
     * 单词算1个长度.使用哪一种计算方式取决于当前正在使用的语言设置。
     * 如果当前设置为中文(zh),即使mess="how are you" 
     * @param mess
     * @return 
     */
    public static int getWorldLength(String mess) {
        String locale = ResManager.getLocale();
        if (locale.startsWith("zh_")) {
            return mess.length();
        } else if (locale.startsWith("en_")) {
            return mess.split("\\s+").length;
        }
        throw new UnsupportedOperationException("Unknow supported locale:" + locale);
    }
    
    
}

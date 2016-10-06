/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.layer.service.ConfigService;

/**
 * @deprecated use ResManager instead
 * 资源文件读取类
 * @author huliqing
 */
public class ResourceManager {
    
    // 默认的资源文件: /data/font/resource
    private static Map<String, String> resource;
    
    // 其它资源文件
    private final static Map<String, Map<String, String>> OTHER_RESOURCE = new HashMap<String, Map<String, String>>();
    
    /**
     * 从默认资源文件中获取资源
     * @param key
     * @return 
     */
    public static String get(String key) {
        return get(key, null);
    }
    
    /**
     * 从默认资源文件中获取资源
     * @param key
     * @param params
     * @return 
     */
    public static String get(String key, Object[] params) {
        if (resource == null) {
            resource = loadResource();
        }
        return getString(resource, key, params);
    }
    
    public static String getOther(String resourceName, String key) {
        return getOther(resourceName, key, null);
    }
    
    /**
     * 从其它资源文件中获取资源。
     * @param resourceName 
     *  资源文件名称，如果不是绝对路径，则默认会从 /data/font/下查找资源文件。
     * @param key
     * @param params
     * @return 
     */
    public static String getOther(String resourceName, String key, Object[] params) {
        Map<String, String> otherResource = OTHER_RESOURCE.get(resourceName);
        if (otherResource == null) {
            otherResource = loadResource(resourceName);
            OTHER_RESOURCE.put(resourceName, otherResource);
        }
        return getString(otherResource, key, params);
    }
    
    /**
     * 清理所有已经载入的文本信息，当切换了语言环境时可以调用该方法。
     */
    public static void clearResources() {
        resource = null;
        OTHER_RESOURCE.clear();
    }
    
    private static String getString(Map<String, String> resource, String key, Object[] params) {
        String value = resource.get(key);
        if (value == null) {
            value = "<" + key + ">";
        }
        if (params != null) {
            value = String.format(value, params);
        }
//        Logger.get(ResourceManager.class).log(Level.INFO, "getString={0}", value);
        return value;
    }
    
    /**
     * 载入资源文件，如果resource指定的名称不是绝对路径，则默认从/data/font/下查找资源文件。
     * 使用"/"开头来指定绝对资源文件的路径。
     * @param resource
     * @return 
     */
    private static Map<String, String> loadResource(String resource) {
        if (!resource.startsWith("/")) {
            ConfigService configService = Factory.get(ConfigService.class);
            String locale = configService.getLocale();
            resource = "/data/font/" + locale + "/" + resource;
        }
        
        BufferedReader br = null;
        Map<String, String> result = new HashMap<String, String>();
        try {
            InputStream is = ResourceManager.class.getResourceAsStream(resource);
            // 必须指定编码格式，否则在非utf-8平台（如win）下中文会乱码。
            br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            int index;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                index = line.indexOf("=");
                if (index == -1) {
                    continue;
                }
                String key = line.substring(0, index).trim();
//                String value = line.substring(index + 1).trim();// remove
                String value = line.substring(index + 1); // 没有trim()
                result.put(key, value);
            }
        } catch (IOException ioe) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, ioe.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage());
                }
            }
        }
        return result;
    }
    
    private static Map<String, String> loadResource() {
        Map<String, String> temp = new HashMap<String, String>();
        temp.putAll(loadResource("resource"));
        temp.putAll(loadResource("resource_object"));
        return temp;
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
        ConfigService configService = Factory.get(ConfigService.class);
        String locale = configService.getLocale();
        if (locale.startsWith("zh_")) {
            return mess.length();
        } else if (locale.startsWith("en_")) {
            return mess.split("\\s+").length;
        }
        throw new UnsupportedOperationException("Unknow supported locale:" + locale);
    }
    
    
}

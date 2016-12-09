/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.export.Savable;
import name.huliqing.luoying.Inject;

/**
 * 存档服务
 * @author huliqing
 */
public interface SaveService extends Inject {
    
    /**
     * 把字节数据使用指定的key保存起来
     * @param key
     * @param data 
     */
    void save(String key, byte[] data);
    
    /**
     * 删除一个存档。
     * @param key 
     */
    void delete(String key);
    
    /**
     * 载入key所指定的数据,如果不存在指定key的数据则返回null.
     * @param key
     * @return 
     */
    byte[] load(String key);
    
    /**
     * 判断是否存在指定的保存的key,如果存在则返回true,否则false
     * @param key
     * @return 
     */
    boolean existsSaveKey(String key);
    
    /**
     * 保存Savable格式的数据。
     * @param key
     * @param data 
     */
    void saveSavable(String key, Savable data);
    
    /**
     * 载入Savable格式类型的存档。
     * @param <T>
     * @param key
     * @return 
     */
    <T extends Savable> T loadSavable(String key);
    
}

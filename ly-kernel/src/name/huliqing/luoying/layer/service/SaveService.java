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

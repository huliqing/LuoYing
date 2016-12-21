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
package name.huliqing.luoying.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.xml.SimpleCloner;

/**
 * SavableArrayList主要用于封装集合类型的数据为一个<b>单独的</b>Savable对象，
 * 避免ObjectData去直接设置一个集合类型的对象(由于直接向objectData设置集合类型数据时容易出现一些异常现象)<br>
 * 使用示例：
 * <code>
 * <pre>
 * SavableArrayList&lt;Savable&gt; list = new SavableArrayList&lt;Savable&gt;();
 * list.add(savable1);
 * list.add(savable2);
 * ...
 * ObjectData objectData = Loader.loadData("xxx");
 * objectData.setAttribute("savableList", list);
 * </pre>
 * </code>
 * @author huliqing
 * @param <T>
 */
@Serializable
public class SavableArrayList<T extends Savable> implements Savable, Cloneable{
    
    private List<T> list;
    
    public SavableArrayList() {}
    
    public SavableArrayList(int initialCapacity) {
        list = new ArrayList<T>(initialCapacity);
    }
    
    public SavableArrayList(Collection<T> c) {
        this(c.size());
        list.addAll(c);
    }
    
    /**
     * 向列表中添加一个数据
     * @param object 
     */
    public void add(T object) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        if (!list.contains(object)) {
            list.add(object);
        }
    }
    
    /**
     * 从列表中移除一个数据
     * @param object
     * @return 
     */
    public boolean remove(T object) {
        return list != null && list.remove(object);
    }
    
    /**
     * 清空列表
     */
    public void clear() {
        if (list != null) {
            list.clear();
        }
    }
    
    /**
     * 获取数据列表，可能返回null.
     * @return 
     */
    public List<T> getList() {
        return list;
    }
    
    /**
     * 判断列表是否为Null或者为空
     * @return 
     */
    public boolean isEmpty() {
        return list == null || list.isEmpty();
    }

    @Override
    public SavableArrayList clone() {
        try {
            SavableArrayList clone = (SavableArrayList) super.clone();
            clone.list = SimpleCloner.deepClone(list);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new LuoYingException(e);
        }
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (list != null) {
            oc.writeSavableArrayList(new ArrayList<T>(list), "list", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        list = ic.readSavableArrayList("list", null);
    }
}

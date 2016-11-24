/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class SavableArrayList<T extends Savable> implements Savable {
    
    private ArrayList<T> list;
    
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
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (list != null) {
            oc.writeSavableArrayList(list, "list", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        list = ic.readSavableArrayList("list", null);
    }
}

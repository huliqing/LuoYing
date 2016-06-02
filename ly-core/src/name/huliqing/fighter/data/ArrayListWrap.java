/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 包装UserData以兼容list类型
 * @author huliqing
 */
public class ArrayListWrap<T> implements Savable {

    private ArrayList<T> inner;
    
    public ArrayListWrap() {
        inner = new ArrayList<T>();
    }
    
    public ArrayListWrap(int initialCapacity) {
        inner = new ArrayList<T>(initialCapacity);
    }
    
    public void add(T t) {
        inner.add(t);
    }
    
    public void add(int index, T t) {
        inner.add(index, t);
    }
    
    public void remove(T t) {
        inner.remove(t);
    }
    
    public void clear() {
        inner.clear();
    }
    
    public boolean contain(T t) {
        return inner.contains(t);
    }
    
    public ArrayList<T> getInnerData() {
        return inner;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        ex.getCapsule(this).writeSavableArrayList(inner, "list", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        ArrayList<T> temp = im.getCapsule(this).readSavableArrayList("list", null);
        inner.clear();
        if (temp != null) {
            inner.addAll(temp);
        }
    }

    @Override
    public String toString() {
        return "ArrayListWrap{" + "inner=" + inner + '}';
    }
    
}

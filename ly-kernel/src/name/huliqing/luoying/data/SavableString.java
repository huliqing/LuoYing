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
import name.huliqing.luoying.LuoYingException;

/**
 * 这个类主要是解决JME在保存中文字符时候的BUG， 一些正常UTF8的中文字符仍然会被错误解读为"ISO8859_1",
 * 这导致保存后再解读的时候乱码，这个类直接把字符串保存成字节码数组，这样就不会导致JME在判断UTF8时候的问题。
 * @author huliqing
 * @see BinaryInputCapsule#readString(byte[])
 */
@Serializable
public final class SavableString implements Savable, Cloneable{

    private String value;
    
    public SavableString() {}

    public SavableString(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        if (value != null) {
            oc.write(value.getBytes(), "v", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        byte[] temp = ic.readByteArray("v", null);
        if (temp != null) {
            value = new String(temp);
        }
    }
    
    @Override
    public SavableString clone() {
        try {
            SavableString clone = (SavableString) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new LuoYingException(e);
        }
    }
}

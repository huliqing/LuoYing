/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.define;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.data.DefineData;

/**
 * 装备各部位的定义
 * @author huliqing
 */
public class SkinPartDefine extends Define {
    private static final Logger LOG = Logger.getLogger(SkinPartDefine.class.getName());

    private final List<String> skinParts = new ArrayList<String>();
    
    @Override
    public void setData(DefineData data) {
        super.setData(data); 
        String[] tempParts = data.getAsArray("skinParts");
        if (tempParts != null) {
            skinParts.addAll(Arrays.asList(tempParts));
        }
    }
    
    /**
     * @return 
     */
    public final int size() {
        return skinParts.size();
    }
    
    /**
     * @param parts
     * @return 
     */
    public long convert(String... parts) {
        long result = 0L;
        int idx;
        for (String p : parts) {
            idx = skinParts.indexOf(p);
            if (idx != -1) {
                result |= 1L << idx;
            }
        }
        return result;
    }
    
    /**
     * 注册一个新的装备部位
     * @param skinPart 
     */
    public synchronized void registerSkinPart(String skinPart) {
        if (skinParts.contains(skinPart)) {
            LOG.log(Level.WARNING, "Could not register skin part,  skin part already exists! skinPart={0}", skinPart);
            return;
        }
        if (size() >= 64) {
            LOG.log(Level.WARNING
                    , "Could not register skin part, the size of skin part could not more than 64! skinPart={0}, current size={1}"
                    , new Object[] {skinPart, size()});
            return;
        }
        skinParts.add(skinPart);
    }
    
     /**
     * 清理
     */
    public synchronized void clear() {
        skinParts.clear();
    }
    
    @Override
    public String toString() {
        return skinParts.toString();
    }
    
    /**
     * 查询parts所代表的各个装备部位的名称。
     * @param parts
     * @return 
     */
    public String toString(long parts) {
        List<String> temp = new ArrayList<String>();
        for (int i = 0; i < size(); i++) {
            if ((parts & 1 << i) != 0) {
                temp.add(skinParts.get(i));
            }
        }
        return temp.toString();
    }
}

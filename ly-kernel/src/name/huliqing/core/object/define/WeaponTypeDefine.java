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
 * 武器类型的定义
 * @author huliqing
 */
public class WeaponTypeDefine extends Define {
    private static final Logger LOG = Logger.getLogger(WeaponTypeDefine.class.getName());

    private final List<String> typeList = new ArrayList<String>();
    
    @Override
    public void setData(DefineData data) {
        super.setData(data); 
        String[] tempTypes = data.getAsArray("weaponTypes");
        if (tempTypes != null) {
            typeList.addAll(Arrays.asList(tempTypes));
        }
    }
    
    /**
     * @return 
     */
    public final int size() {
        return typeList.size();
    }
    
    /**
     * @param weaponTypes
     * @return 
     */
    public long convert(String... weaponTypes) {
        long result = 0L;
        int idx;
        for (String p : weaponTypes) {
            idx = typeList.indexOf(p);
            if (idx != -1) {
                result |= 1L << idx;
            }
        }
        return result;
    }
    
    /**
     * @param weaponType 
     */
    public synchronized void registerSkinPart(String weaponType) {
        if (typeList.contains(weaponType)) {
            LOG.log(Level.WARNING, "Could not register weapon type,  weapon type already exists! weaponType={0}", weaponType);
            return;
        }
        if (size() >= 64) {
            LOG.log(Level.WARNING
                    , "Could not register weapon type, the size of weapon type could not more than 64! weaponType={0}, current size={1}"
                    , new Object[] {weaponType, size()});
            return;
        }
        typeList.add(weaponType);
    }
    
    public synchronized void clear() {
        typeList.clear();
    }
    
    @Override
    public String toString() {
        return typeList.toString();
    }
    
    /**
     * @param weaponTypes
     * @return 
     */
    public String toString(long weaponTypes) {
        List<String> temp = new ArrayList<String>();
        for (int i = 0; i < size(); i++) {
            if ((weaponTypes & 1 << i) != 0) {
                temp.add(typeList.get(i));
            }
        }
        return temp.toString();
    }
}

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
package name.huliqing.luoying.object.define;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.DefineData;

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
        if (tempTypes != null && tempTypes.length > 0) {
            for (String type : tempTypes) {
                registerWeaponType(type);
            }
        } else {
            LOG.log(Level.WARNING, "weaponTypes not defined.");
        }
    }
    
    /**
     * @return 
     */
    public final int size() {
        return typeList.size();
    }
    
    /**
     * 把武器类型转化为二进制表示形式，返回的整形中每个二进制位(1)表示一个武器类型。
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
     * 注册一个新的武器类型，这个方法必须在系统初始化时调用注册。
     * @param weaponType 
     */
    public synchronized void registerWeaponType(String weaponType) {
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
    
    /**
     * 清理所有”技能类型“的定义
     */
    public synchronized void clear() {
        typeList.clear();
    }
    
    @Override
    public String toString() {
        return typeList.toString();
    }
    
    /**
     * 显示weaponTypes中所代表的各种武器类型，以字符串形式返回.
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

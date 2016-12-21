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
package name.huliqing.luoying.enums;

/**
 * 定义角色(character)的性别
 * @author huliqing
 */
public enum Sex {
    /** 女性 */
    female(0), 

    /** 男性 */
    male(1),
    
    /** 无性别类型 */
    unknow(2);
    
    private final int value;
    
    private Sex(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public static Sex identifyByName(String value) {
        if (value == null || value.trim().length() <= 0) {
            return null;
        }
       return identify(Integer.parseInt(value));
    }
    
    public static Sex identify(int value) {
       Sex[] ss = Sex.values();
       for (Sex s : ss) {
           if (s.getValue() == value) {
               return s;
           }
       }
       throw new UnsupportedOperationException("不支持的Sex类型:" + value);
    }
    
    /**
     * 使用"f","m"来判断性别。f=female, m=male, 其它为unknow.不区别大小写。
     * 如 identifyByFM("f")=Sex.female
     * @param fm
     * @return 
     */
    public static Sex identifyByFM(String fm) {
        if ("f".equalsIgnoreCase(fm)) {
            return Sex.female;
        } else if ("m".equalsIgnoreCase(fm)) {
            return Sex.male;
        } else {
            return Sex.unknow;
        }
    }
}

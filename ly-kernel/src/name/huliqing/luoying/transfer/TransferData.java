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
package name.huliqing.luoying.transfer;

import name.huliqing.luoying.xml.ObjectData;

/**
 * 在Transfer中交换的封装数据。
 * @author huliqing
 */
public class TransferData {
    
    private ObjectData objectData;
    private int amount;
     
    /**
     * 获取交换的物体
     * @return 
     */
    public ObjectData getObjectData() {
        return objectData;
    }

    /**
     * 设置交换的物体
     * @param object 
     */
    public void setObjectData(ObjectData object) {
        this.objectData = object;
    }

    /**
     * 获取物体的数量
     * @return 
     */
    public int getAmount() {
        return amount;
    }

    /**
     * 设置物体数量
     * @param amount 
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}

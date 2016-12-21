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

import java.util.List;

/**
 * Transfer定义数据交换传输接口，两个关联的Transfer可以相互之间转移、交换物品。
 * @author huliqing
 */
public interface Transfer {
    
    /**
     * 设置要传输的目标对象
     * @param target 
     */
    void setTarget(Transfer target);
    
    /**
     * 获取要传输的目标对象
     * @return 
     */
    Transfer getTarget();
    
    /**
     * 获取当前Transfer的数据列表。
     * @return 
     */
    List<TransferData> getDatas();
    
    /**
     * 设置数据列表
     * @param datas 
     */
    void setDatas(List<TransferData> datas);
    
    /**
     * 添加数据到列表
     * @param data
     * @param count 添加的数量
     */
    void addData(TransferData data, int count);
    
    /**
     * 从列表中移除数据。
     * @param data
     * @param count 移除的数量
     */
    void removeData(TransferData data, int count);
    
    /**
     * 通过id查找data,如果不存在则返回null.
     * @param id
     * @return 
     */
    TransferData findData(String id);
    
    /**
     * 传输数据到目标transfer(target)
     * @param data 传输的目标数据
     * @param count 传输的数量
     */
    void transfer(TransferData data, int count);
    
    /**
     * 添加数据传输侦听器
     * @param listener 
     */
    void addListener(TransferListener listener);
    
    /**
     * 移除数据传输侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(TransferListener listener);
    
    
}

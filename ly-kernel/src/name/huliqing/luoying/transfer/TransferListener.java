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

/**
 * 交易侦听器
 * @author huliqing
 */
public interface TransferListener {
    
    /**
     * 当transfer添加了一个物体的时候这个方法会被调用。
     * @param transfer
     * @param data
     * @param count 
     */
    void onAdded(Transfer transfer, TransferData data, int count);
    
    /**
     * 当transfer移除掉一个物体的时候这个方法会被调用。
     * @param transfer
     * @param data
     * @param count 
     */
    void onRemoved(Transfer transfer, TransferData data, int count);
}

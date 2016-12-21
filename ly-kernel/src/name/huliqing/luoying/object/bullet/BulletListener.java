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
package name.huliqing.luoying.object.bullet;

/**
 * @author huliqing
 */
public interface BulletListener {
    
    /**
     * 在子弹的飞行过程中，该方法会持续被调用,可以在这个方法中来检测子弹是否击中一个目标，
     * 如果击中了一个目标，则返回true,否则返回false. 如果击中了一个目标，并希望让子弹结束，
     * 可以调用 {@link #consume() }方法来销毁子弹。
     * @param bullet 
     * @return  
     */
    boolean onBulletFlying(Bullet bullet);
    
}

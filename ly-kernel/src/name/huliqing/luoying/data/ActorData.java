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
package name.huliqing.luoying.data;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.data.define.MatObject;

/**
 * @author huliqing
 */
@Serializable
public class ActorData extends ModelEntityData implements MatObject {
    
    /**
     * 获取文件模型
     * @return 
     */
    public String getFile() {
        return getAsString("file");
    }
    
//    /**
//     * 扩展的骨骼动画目标路径，这个参数指向一个asset中的目录,
//     * 如："Models/actor/anim" 当角色使用的技能中找不到相应的动画时将会从这个目录中查找动画文件
//     * @return 
//     */
//    public String getExtAnim() {
//        return getAsString("extAnim");
//    }
    
    /**
     * 指定角色原始视角方向,默认情况下为(0,0,1),如果模型默认不是该方向,则需要使用该方向指定模型的正视角方向。
     * @return 
     */
    public Vector3f getLocalForward() {
        return getAsVector3f("localForward");
    }
    
}

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
package name.huliqing.luoying.object.anim;

import com.jme3.scene.Node;

/**
 * 用于包装运行Anim
 * @author huliqing
 */
public class AnimNode extends Node {

    private final Anim anim;
    private boolean autoDetach;

    public AnimNode(Anim anim, boolean autoDetach) {
        this.anim = anim;
        this.autoDetach = autoDetach;
    }

    /**
     * 设置是否在动画结束时自动从场景脱离。
     * @param autoDetach 
     */
    public void setAutoDetach(boolean autoDetach) {
        this.autoDetach = autoDetach;
    }
            
    @Override
    public final void updateLogicalState(float tpf) {
        if (autoDetach && anim.isEnd()) {
            removeFromParent();
        } else {
            anim.update(tpf);
        }
    }
    
}

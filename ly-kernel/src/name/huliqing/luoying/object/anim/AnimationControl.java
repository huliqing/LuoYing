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

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 * 这个AnimationControl是用来包装Anim的，让Anim可以作为Control直接添加到某个节点下面。
 * @author huliqing
 */
public class AnimationControl extends AbstractControl {
    
    private Anim anim;

    public AnimationControl() {}
    
    public AnimationControl(Anim anim) {
        this.anim = anim;
    }
    
    public Anim getAnim() {
        return anim;
    }

    public void setAnim(Anim anim) {
        this.anim = anim;
    }

    @Override
    protected final void controlUpdate(float tpf) {
        anim.update(tpf);
    }

    @Override
    protected final void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}

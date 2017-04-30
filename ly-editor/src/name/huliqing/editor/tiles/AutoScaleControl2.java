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
package name.huliqing.editor.tiles;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * 动态控制场景中物体的大小
 * @author huliqing
 */
public class AutoScaleControl2 extends AbstractControl {
    
    private Camera camera;
    private final Vector3f fixScale = new Vector3f(1,1,1);
    private final Vector3f tempScale = new Vector3f();
    
    public AutoScaleControl2() {}
    
    public AutoScaleControl2(float fixScale) {
        this.fixScale.set(fixScale, fixScale, fixScale);
    }
    
    public AutoScaleControl2(Vector3f fixScale) {
        this.fixScale.set(fixScale);
    }
    
    public void setFixScale(Vector3f fixScale) {
        this.fixScale.set(fixScale);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        if (camera == null || spatial.getCullHint() == Spatial.CullHint.Always) {
            return;
        }
        tempScale.set(fixScale).divideLocal(spatial.getParent().getWorldScale());
        tempScale.multLocal(spatial.getWorldTranslation().distance(camera.getLocation()));
        spatial.setLocalScale(tempScale);
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        camera = vp.getCamera();
    }
    
}

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
package name.huliqing.luoying.object.module;

import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LodControl;
import java.util.HashSet;
import java.util.Set;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.xml.ObjectData;

/**
 * Level of Detail Module
 * @author huliqing
 */
public class LodModule extends AbstractModule {

    private final Set<LodControl> lcs = new HashSet<LodControl>();
    private float distTolerance = 1.0f;
    private float trisPerPixel = 1.0f;
    
    @Override
    public void setData(ModuleData data) {
        super.setData(data);
        distTolerance = data.getAsFloat("distTolerance", distTolerance);
        trisPerPixel = data.getAsFloat("trisPerPixel", trisPerPixel);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        entity.getSpatial().depthFirstTraversal(new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (spatial instanceof Geometry) {
                    if (spatial.getControl(LodControl.class) == null) {
                        LodControl lod = new MyLodControl();
                        lod.setDistTolerance(distTolerance);
                        lod.setTrisPerPixel(trisPerPixel);
                        lod.setEnabled(enabled);
                        spatial.addControl(lod);
                        lcs.add(lod);
                    }
                }
            }
        });
    }
    
    @Override
    public void cleanup() {
        for (LodControl lc : lcs) {
            lc.getSpatial().removeControl(lc);
        }
        lcs.clear();
        super.cleanup();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (LodControl lc : lcs) {
            lc.setEnabled(enabled);
        }
    }
    
    @Override
    public boolean handleDataAdd(ObjectData hData, int amount) {
        return false;
    }

    @Override
    public boolean handleDataRemove(ObjectData hData, int amount) {
        return false;
    }

    @Override
    public boolean handleDataUse(ObjectData hData) {
        return false;
    }
    
    private class MyLodControl extends LodControl {
        @Override
        public void setSpatial(Spatial spatial) {
            // 默认的LodControl在从Spatial移除时会报错。
            // Spatial在移除Control时会向Control设置setSpatial(null),
            // 但是在LodControl中会判断spatial是否为Geometry类型,这会导致报错。
            if (spatial == null) {
                this.spatial = null;
                return;
            }
            super.setSpatial(spatial);
        }
    }
}

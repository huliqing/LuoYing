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
package name.huliqing.luoying.object;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 * AbstractControl 适配器, 只保留update方法，其它任何方法都不要覆盖。
 * @author huliqing
 */
public class ControlAdapter implements Control {

    @Override
    public final Control cloneForSpatial(Spatial spatial) {
        // ignore
        return null;
    }

    @Override
    public final void setSpatial(Spatial spatial) {
        // ignore
    }

    @Override
    public void update(float tpf) {
        // for children override
    }

    @Override
    public final void render(RenderManager rm, ViewPort vp) {
        // ignore
    }

    @Override
    public final void write(JmeExporter ex) throws IOException {
        // ignore
    }

    @Override
    public final void read(JmeImporter im) throws IOException {
        // ignore
    }

  
    
}

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
package name.huliqing.editor.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.edit.controls.entity.AdvanceWaterEntityControlTile;
import name.huliqing.editor.edit.controls.entity.DirectionalLightEntityControlTile;
import name.huliqing.editor.edit.controls.entity.EmptyEntityControlTile;
import name.huliqing.editor.edit.controls.entity.EntityControlTile;
import name.huliqing.editor.edit.controls.entity.SimpleModelEntityControlTile;
import name.huliqing.luoying.data.EntityData;

/**
 *
 * @author huliqing
 */
public class ControlTileManager {

    private static final Logger LOG = Logger.getLogger(ControlTileManager.class.getName());
    
    private final static Map<String, Class<? extends EntityControlTile>> MAPPING = new HashMap();
    
    static {
        MAPPING.put("entitySimpleModel", SimpleModelEntityControlTile.class);
        MAPPING.put("entitySimpleWater", SimpleModelEntityControlTile.class);
        MAPPING.put("entitySimpleTerrain", SimpleModelEntityControlTile.class);
        MAPPING.put("entityTree", SimpleModelEntityControlTile.class);
        MAPPING.put("entityGrass", SimpleModelEntityControlTile.class);
        MAPPING.put("entityEffect", SimpleModelEntityControlTile.class);
        MAPPING.put("entityDirectionalLight", DirectionalLightEntityControlTile.class);
        MAPPING.put("entityAdvanceWater", AdvanceWaterEntityControlTile.class);
        
        MAPPING.put("entitySkyBox", EmptyEntityControlTile.class);
        MAPPING.put("entityAmbientLight", EmptyEntityControlTile.class);
        MAPPING.put("entityDirectionalLightFilterShadow", EmptyEntityControlTile.class);
        MAPPING.put("entityChaseCamera", EmptyEntityControlTile.class);
        MAPPING.put("entityPhysics", EmptyEntityControlTile.class);
    }
    
    public final static <T extends EntityControlTile> T createEntityControlTile(EntityData ed) {
        Class<? extends ControlTile> clazz = MAPPING.get(ed.getTagName());
        if (clazz == null) {
            LOG.log(Level.WARNING, "Could not find controlTile Mapping, unknow tagName={0}", ed.getTagName());
//            return (T) new EmptyEntityControlTile();
            return (T) new SimpleModelEntityControlTile();
        }
         
        ControlTile obj;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
//            obj = new EmptyEntityControlTile();
            obj = new SimpleModelEntityControlTile();
            Logger.getLogger(ControlTileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) obj;
    }
    
    
}
